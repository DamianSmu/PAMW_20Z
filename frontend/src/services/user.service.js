import axios from 'axios';
//import authHeader from './auth-header';

const API_URL = 'http://localhost:8080/api/user/';

class UserService {
   /* getPublicContent() {
        return axios.get(API_URL + 'all');
    }

    getUserBoard() {
        return axios.get(API_URL + 'user', { headers: authHeader() });
    }

    getAdminBoard() {
        return axios.get(API_URL + 'admin', { headers: authHeader() });
    }*/

    getLoginAvailability(value){
        return axios.get(API_URL + 'check/' + value).then(response => {
            if(response.status !== 200){
                return "error";
            } else{
                return response.data.message;
            }
                
        });
    }
}

export default new UserService();