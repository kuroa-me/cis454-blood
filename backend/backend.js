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
const FS = require('fs');
const PATH = require('path');

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

api.misc.getbloodtypes = async function (req, res) {
    res.writeHead(200, {'Content-Type': 'application/json'});

    var result = await sql('query', 'select * from blood_type');
    res.end(JSON.stringify({
        ok: true,
        types: result
    }));
};
handlers.push({path: '/misc/getbloodtypes', handler: api.misc.getbloodtypes});

api.user.auth = async function (req, res) {
    var { username, password } = req.body;

    if (!username || !password) {
        res.end(JSON.stringify({
            ok: false,
            error: 'Empty username or password.'
        }));
        return;
    }

    var result = await sql('query', 'select id from user where password = MD5(?) and username = ?',
                           [password, username]);

    if (result.length == 0) {
        res.end(JSON.stringify({
            ok: false,
            error: 'Incorrect username or password.'
        }));
        return;
    }

    console.log(result);

    if (result.length > 1) throw "mutiple user match.";

    res.end(JSON.stringify({
        ok: true,
        token: session.new(result[0].id)
    }));
};
handlers.push({path: '/user/auth', handler: api.user.auth});

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

const sql = function(fn, ...args) {
    return new Promise ((res, rej) => {
        SQL[fn](...args, (e, r, f) => {
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

const root_handler = function (req, res) {
    var url = URL.parse(req.url, true);
    var method = req.method;
    var headers = req.headers;
    (new Promise((reso, rej) => {
        if (req.method != 'POST') reso('');
        else {
            var body = '';
            req.on('data', d => body += d);
            req.on('end', () => {
                if (headers["content-type"] == 'application/json') {
                    try {
                        reso(JSON.parse(body));
                    } catch (e) {
                        res.writeHead(404, {'Content-Type': 'application/json'});
                        res.end(JSON.stringify({ok: false, errmsg: "Error: " + e}));
                        rej(e);
                    }
                } else reso({});
            });
            req.on('aborted', rej);
            req.on('close', rej);
        }
    })).then(async body => {
        try {
            await find_handler(url.pathname)({
                path: url.pathname, 
                query: url.query, 
                method, headers, body
            }, res);
        } catch (e) {
            console.log('Handler returned error: ', e);
            res.end(JSON.stringify({
                ok: false,
                error: 'Internal error.'
            }));
        }
        
    }).catch(err => {
        console.log("Error while handling request: "+ err);
    })

    
};

var server = HTTP.createServer(root_handler);
server.listen(API_PORT, API_HOST);