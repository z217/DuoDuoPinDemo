let baseUrl = 'http://123.57.12.189:8080/'
let wsBaseUrl = 'ws://123.57.12.189:8080/'

function sendJSON(method, url, async, data, id, token) {
  let res;
  $.ajax({
    type: method,
    url: url,
    async: async,
    data: JSON.stringify(data),
    dataType: 'json',
    beforeSend: function (xhr) {
      xhr.setRequestHeader('content-type', 'application/json');
      if (id !== undefined && token !== undefined)
        xhr.setRequestHeader('token', id + '_' + token);
    },
    success: function(result) {
      res = result;
    },
    error: function (result) {
      res = result;
    }
  });
  return res;
}
