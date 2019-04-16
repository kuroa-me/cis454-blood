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

var handlers = [];

///////////////////////////////////////////////////////////////////////////////
// MySQL
///////////////////////////////////////////////////////////////////////////////

const sql = MYSQL.createPool({
    host: MYSQL_HOST,
    user: MYSQL_USER,
    password: MYSQL_PASS,
    database: MYSQL_DB,
    connectionLimit: 64
});

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
    })).then(body => {
        find_handler(url.pathname)({
            path: url.pathname, 
            query: url.query, 
            method, headers, body
        }, res);
    }).catch(err => {
        console.log("Error while handling request: "+ err);
    })

    
};

var server = HTTP.createServer(root_handler);
server.listen(API_PORT, API_HOST);