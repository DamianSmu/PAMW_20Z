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



    deleteParcel(id) {
        return axios.delete(API_URL + id,
            {
                headers: {
                    'Content-Type': 'application/json'
                },
                withCredentials: true
            }).then(response => {
                return response.data;
            });
    }

    addParcel(receiver, postOffice, size) {
        return axios.post(API_URL, {
            receiver,
            postOffice,
            size
        }, {
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