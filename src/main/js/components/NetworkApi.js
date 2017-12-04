import axios from 'axios';
import _ from 'lodash';

export default  {

  get(url) {
    const self = this;
    const config = {headers: {'X-Requested-With': 'XMLHttpRequest'}};
    return axios.get(url, config)
                .then(function (response) {
                  self.csrfCatchToken(response);
                  return response.data;
                })
                .catch(function (error) {
                  self.csrfCatchToken(error.response);
                  throw error.response;
                });
  },
  post(url, command) {
    const self = this;
    const config = this.insertCsrfToken({headers: {'X-Requested-With': 'XMLHttpRequest'}});
    const cmd  =  _.assign({}, command);
    return axios.post(url, cmd, config)
                .then(function (response) {
                  self.csrfCatchToken(response);
                   return response.data;
                })
                .catch(function (error) {
                  self.csrfCatchToken(error.response);
                   throw error.response;
                });
  },
  delete(url1, item) {
    const self = this;
    const config = this.insertCsrfToken({headers: {'X-Requested-With': 'XMLHttpRequest'}})
    const url = url1 + '/' + item.id;
    return axios.delete(url, config)
                .then(function (response) {
                  self.csrfCatchToken(response);
                  return response.data;
                })
                .catch(function (error) {
                  self.csrfCatchToken(error.response);
                  throw error.response;
                });
  },
  put(url1, command) {
    const self = this;
    const config = this.insertCsrfToken({headers: {'X-Requested-With': 'XMLHttpRequest'}});
    const cmd = _.assign({}, command);
    const url = url1 + '/' + cmd.id;
    return axios.put(url, cmd, config)
                .then(function (response) {
                  self.csrfCatchToken(response);
                  return response.data;
                })
                .catch(function (error) {
                  self.csrfCatchToken(error.response);
                  throw error.response;
                });
  },
  sendLogin(username, password) {
    const self = this;
    const creds = "Basic " + btoa(username + ":" + password);
    const config = {
      headers: {
        'X-Requested-With': 'XMLHttpRequest',
      'authorization': creds
      }
    };
    return axios.get('api/accounts', config)
                .then(function (response) {
                  self.csrfCatchToken(response);
                  return response.data;
                })
                .catch(function (error) {
                  self.csrfCatchToken(error.response);
                  throw error.response;
                });
  },
  signup(username, password) {
    const self = this;
    const command = {username: username, password: password, roles: [{"id":1,"name":"USER"}]};
    const config = this.insertCsrfToken({headers: {'X-Requested-With': 'XMLHttpRequest'}});
    return axios.post('api/accounts/signup', command, config)
         .then(function (response) {
           self.csrfCatchToken(response);
           return response.data;
         })
         .catch(function (error) {
           self.csrfCatchToken(error.response);
           throw error.response;
         });
  },
  csrfCatchToken(response) {
    if (undefined !== response.headers) {
      const headers = response.headers;
      if (undefined !== headers['x-csrf-header'] && undefined !== headers['x-csrf-token']) {
        const token = headers['x-csrf-token'];
        const headerName = headers['x-csrf-header'];
        this.csrfToken = token;
        if (this.csrfHeaderName !== headerName) {
          this.csrfHeaderName = headerName;
        }
      }
    }
  },
  csrfToken: "",
  csrfHeaderName: "X-CSRF-TOKEN",
  insertCsrfToken(config) {
    if ("" !== this.csrfToken) {
      const headerName = this.csrfHeaderName;
      const newHeaders = _.assign({}, config.headers);
      newHeaders[headerName] = this.csrfToken;
      const newConfig = _.assign({}, config, {headers: newHeaders});
      return newConfig;
    }
    return config;
  }
}
