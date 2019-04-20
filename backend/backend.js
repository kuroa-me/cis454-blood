///////////////////////////////////////////////////////////////////////////////
// Bllod system Backend Server
// Author: Honghao Zeng <hozeng@syr.edu>

///////////////////////////////////////////////////////////////////////////////
// Configuration
///////////////////////////////////////////////////////////////////////////////

const MYSQL_HOST = 'localhost';
const MYSQL_USER = 'root';
const MYSQL_PASS = 'toor';
const MYSQL_DB = 'blood';
const API_PORT = 8080;
const API_HOST = '127.0.0.1';

///////////////////////////////////////////////////////////////////////////////
// Modules Load
///////////////////////////////////////////////////////////////////////////////

const HTTP = require('http');
const URL = require('url');
const MYSQL = require('mysql');
const UUID = require('uuid');
const MD5 = require('md5');

///////////////////////////////////////////////////////////////////////////////
// Session Manager
///////////////////////////////////////////////////////////////////////////////

const SessionManager = function () {
    var sessions = [];

    return {
        new: function(id) {
            var token = UUID.v4();
            var candidates = sessions.filter(s => s.id == id);
            if (candidates.length > 0) {
                candidates[0].token = token;
                return token;
            }
            sessions.push({id, token});
            return token;
        },
        get: function(token) {
            if (!token) return false;
            var candidates = sessions.filter(s => s.token == token);
            return candidates.length > 0 ? candidates[0] : false; 
        },
        remove: function(token) {
            if (!token) return false;
            sessions = sessions.filter(s => s.token != token);
        }
    };
}

var session = new SessionManager();

///////////////////////////////////////////////////////////////////////////////
// Handlers
///////////////////////////////////////////////////////////////////////////////

var api = {
    misc: {},
    user: {},
    donor: {},
    requester: {},
    admin: {}
};

var handlers = [];

// Misc ///////////////////////////////////////////////////////////////////////

api.misc.getbloodtypes = async function (req, res) {
    var result = await sql('select * from blood_type');
    return {
        ok: true,
        types: result
    };
};
handlers.push({path: '/misc/getbloodtypes', handler: api.misc.getbloodtypes});

// User ///////////////////////////////////////////////////////////////////////

api.user.auth = async function (req, res) {
    var { username, password } = req.body;

    if (!username || !password) {
        return {
            ok: false,
            error: 'Empty username or password.'
        };
    }

    var result = await sql('select id from user where password = MD5(?) and username = ?',
                           [password, username]);

    if (result.length == 0) {
        return {
            ok: false,
            error: 'Incorrect username or password.'
        };
    }

    if (result.length > 1) throw "mutiple user match.";

    return {
        ok: true,
        token: session.new(result[0].id)
    };
};
handlers.push({path: '/user/auth', handler: api.user.auth});

api.user.register = async function (req, res) {
    var { username, password, is_donor, first_name, last_name, blood_type, age, sex, height } = req.body;

    if (!username || !password || !first_name || !last_name || !blood_type || !age || !sex || !height) {
        return {
            ok: false,
            error: 'Missing info.'
        };
    }

    var user = await sql('select id from user where username = ?', [username]);

    if (user.length > 0) {
        return {
            ok: false,
            error: 'User already exist.'
        };
    }

    await sql('insert into user SET ?', {
        username, first_name, last_name, password: MD5(password), type: is_donor ? 'DONOR' : 'REQUESTER'
    });

    var new_user = await sql('select id from user where username = ?', [username]);

    if (new_user.length > 1) throw "after register, mutiple user with id " + username + " exist.";

    var new_user_id = new_user[0].id;

    await sql('insert into user_info SET ?', {
        user_id: new_user_id, blood_type, sex, age, height
    });

    return {
        ok: true,
        token: session.new(new_user_id)
    };
};
handlers.push({path: '/user/register', handler: api.user.register});

api.user.get = async function (req, res) {
    var s = session.get(req.body.token);

    if (!s) {
        return {
            ok: false,
            error: 'Permission denied.'
        };
    }

    var user_base = await sql('select username, first_name, last_name, type from user where id = ?', [s.id]);
    var user_info = await sql('select blood_type, age, sex, height from user_info where user_id = ?', [s.id]);

    if (user_base.length != 1 || user_info.length != 1)
        throw "user_base/user_info length not 1.";

    var result = Object.assign(user_base[0], user_info[0]);

    return Object.assign({ok: true}, result);
};
handlers.push({path: '/user/get', handler: api.user.get});

api.user.update = async function (req, res) {
    var s = session.get(req.body.token);

    if (!s) {
        return {
            ok: false,
            error: 'Permission denied.'
        };
    }

    var { password, first_name, last_name, blood_type, age, sex, height } = req.body;

    var query = '', vars = [];

    if (!first_name || !last_name || !blood_type || !age || !sex || !height) {
        return {
            ok: false,
            error: 'Missing info.'
        };
    }

    if (!password || password == '') {
        query = 'update user SET first_name = ?, last_name = ? where id = ?';
        vars = [first_name, last_name, s.id];
    } else {
        query = 'update user SET first_name = ?, last_name = ?, password = MD5(?) where id = ?';
        vars = [first_name, last_name, password, s.id];
    }

    await Promise.all([
        sql(query, vars), 
        sql('update user_info SET blood_type = ?, age = ?, sex = ?, height = ? where user_id = ?',
            [blood_type, age, sex, height, s.id])
    ]);

    return {ok: true};

};
handlers.push({path: '/user/update', handler: api.user.update});

api.user.logout = async function (req, res) {
    session.remove(req.body.token);
    return {ok: true};
};
handlers.push({path: '/user/logout', handler: api.user.logout});

// Donor //////////////////////////////////////////////////////////////////////

api.donor.donate = async function (req, res) {
    var s = session.get(req.body.token);

    if (!s) {
        return {
            ok: false,
            error: 'Permission denied.'
        };
    }

    var { timestamp } = req.body;

    if (!timestamp) {
        return {
            ok: false,
            error: 'Missing info.'
        };
    }

    var result = await sql('select type from user where id = ?', [s.id]);
    if (result.length != 1) throw "user.length not 1."

    var infos = await sql('select blood_type from user_info where user_id = ?', [s.id]);
    if (infos.length != 1) throw "user.length not 1."

    var user = result[0];
    if (user.type != 'DONOR') {
        return {
            ok: false,
            error: 'Only donor can donate.'
        };
    }

    var info = infos[0];

    await sql('insert into blood SET ?', {
        from_id: s.id, date_received: timestamp, blood_type: info.blood_type
    });

    return {ok: true};
};
handlers.push({path: '/donor/donate', handler: api.donor.donate});

api.donor.get_donates = async function(req, res) {
    var s = session.get(req.body.token);

    if (!s) {
        return {
            ok: false,
            error: 'Permission denied.'
        };
    }

    var donate_history = await sql('select date_received, to_id, avaliable, date_used from blood where from_id = ?', [s.id]);

    var history = donate_history.map(hist => {
        return {
            date_received: hist.date_received,
            used: !hist.avaliable,
            date_used: hist.date_used,
            to_id: hist.to_id
        };
    });

    return {
        ok: true,
        history
    };
};
handlers.push({path: '/donor/get_donates', handler: api.donor.get_donates});

api.donor.request = {};

api.donor.request.list = async function (req, res) {
    var s = session.get(req.body.token);

    if (!s) {
        return {
            ok: false,
            error: 'Permission denied.'
        };
    }

    var user = await sql('select type from user where id = ?', [s.id]);

    if (user.length != 1) throw "user.len != 1";
    if (user[0].type != 'DONOR') {
        return {
            ok: false,
            error: 'Only donor can list requests.'
        };
    }

    var user_info = await sql('select blood_type from user_info where user_id = ?', [s.id]);

    if (user_info.length != 1) throw "info.len != 1";
    
    var bt = user_info[0].blood_type;
    var request_ids = await sql('select id, by_user from request where blood_type = ? and accepted = 0', bt);
    var request_uids_arr = request_ids.map(r => r.by_user);
    var requester_names = await sql('select id, first_name, last_name from user where id in ?', [[request_uids_arr]]);
    var requester_infos = await sql('select user_id, age, sex from user_info where user_id in ?', [[request_uids_arr]]);

    if (requester_names.length != requester_infos.length) throw "reqn.len != reqi.len";
    var requests = requester_names.map(r => {
        var reqid = request_ids.filter(rid => r.id == rid.by_user)[0].id;
        var req_info = requester_infos.filter(rinfo => r.id == rinfo.user_id)[0];

        return {
            id: reqid,
            requester_name: `${r.first_name} ${r.last_name}`,
            requester_age: req_info.age,
            requester_sex: req_info.sex
        };
    });

    return {
        ok: true,
        requests
    };
    
};
handlers.push({path: '/donor/request/list', handler: api.donor.request.list});

///////////////////////////////////////////////////////////////////////////////
// MySQL
///////////////////////////////////////////////////////////////////////////////

const SQL = MYSQL.createPool({
    host: MYSQL_HOST,
    user: MYSQL_USER,
    password: MYSQL_PASS,
    database: MYSQL_DB,
    connectionLimit: 64
});

const sql = function(...args) {
    return new Promise ((res, rej) => {
        SQL.query(...args, (e, r, f) => {
            if (e) rej(e);
            else res(r);
        });
    });
};

///////////////////////////////////////////////////////////////////////////////
// HTTP
///////////////////////////////////////////////////////////////////////////////
const find_handler = function(path) {
    console.log(`looking for a handler for ${path}.`);
    var candidates = handlers.filter(f => f.path.test ? f.path.test(path) : f.path == path);
    if (candidates.length > 1) console.log(`warning: mutiple handlers for ${path}`);
    if (candidates.length > 0) {
        console.log(`handler of ${path} found, passing control to it.`);
        return candidates[0].handler;
    }
    else return function (req, res) {
        console.log(`got a request to ${req.path}, but can't find a handler for it.`);
        res.writeHead(404, {'Content-Type': 'text/plain'});
        res.write(`Not Found`);
        res.end();
    };
};

const body_fetch = function (req) {
    var headers = req.headers;

    return new Promise((reslove, reject) => {
        if (req.method != 'POST') {
            reject("unsupported HTTP method.");
            return;
        }
        var body = '';
        req.on('data', d => body += d);
        req.on('end', () => {
            if (headers["content-type"] == 'application/json') {
                try {
                    reslove(JSON.parse(body));
                } catch (e) {
                    reject(e);
                }
            } else reject("unsupported content-type.");
        });
        req.on('abort', reject);
        req.on('close', reject);
    });
};

const root_handler = async function (req, res) {
    var url = URL.parse(req.url, true);
    var method = req.method;
    var headers = req.headers;
    
    try {
        var body = await body_fetch(req);
        res.writeHead(200, {'Content-Type': 'application/json'});
        res.end(JSON.stringify(await find_handler(url.pathname)({
            path: url.pathname, 
            query: url.query, 
            method, headers, body
        }, res)));
    } catch (e) {
        console.log('Error Handling Request: ', e);
        res.end(JSON.stringify({
            ok: false,
            error: 'Internal error.'
        }));
    }

};

var server = HTTP.createServer(root_handler);
server.listen(API_PORT, API_HOST);