import axios from 'axios';


const API_URL = 'http://localhost:8080/api/parcel/';

class ParcelService {
   
    getAll() {
        return axios.get(API_URL + "all", {
            headers: {
                'Content-Type': 'application/json'
            },
            withCredentials: true
        }).then(response => {
            return response.data;
        });
    }
}

export default new ParcelService();