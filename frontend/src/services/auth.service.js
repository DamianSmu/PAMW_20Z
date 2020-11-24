import axios from "axios";
axios.defaults.withCredentials = true;

const API_URL = "http://localhost:8080/api/auth/";

class AuthService {
    login(username, password) {
        //axios.defaults.withCredentials = true;

        return axios.post(API_URL + "signin", {
            username,
            password
        }, {
            headers: {
                'Content-Type': 'application/json'
            },
            withCredentials: true
        }).then(response => {
            if (response.data) {
                localStorage.setItem("user", JSON.stringify(response.data));
            }

            return response.data;
        });
    
    }

    logout() {
        window.localStorage.removeItem('user')
        return axios.get(API_URL + "logout", {
            headers: {
                'Content-Type': 'application/json'
            },
            withCredentials: true
        }).then(response => {
                return response.data.message;
        });
    }

    register(firstname, lastname, username, email, password, address) {
        return axios.post(API_URL + "signup", {
            firstname,
            lastname,
            username,
            email,
            password,
            address
        });
    }

    getCurrentUser() {
        return JSON.parse(localStorage.getItem('user'));;
    }

    

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

export default new AuthService();