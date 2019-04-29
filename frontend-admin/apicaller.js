var APICaller = function (api_server) {
    return function (method, param, cb) {
        var xhr = new XMLHttpRequest();
        xhr.open('POST', api_server + method);
        xhr.setRequestHeader("Content-Type", "application/json");
        xhr.send(JSON.stringify(param));
        xhr.onload = function () {
            if (this.status == 200) {
                cb(JSON.parse(xhr.response));
            }
        };
    };
};