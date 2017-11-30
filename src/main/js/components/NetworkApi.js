import axios from 'axios';

export default  {

  get(url) {
    const config = {headers: {'X-Requested-With': 'XMLHttpRequest'}};
    const self = this;

    return axios.get(url, config)
         .then(function (response) {
            return response.data;
         })
        .catch(function (error) {
           throw error;
        });
  }
}
