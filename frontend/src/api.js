import axios from 'axios';

const API = axios.create({
    baseURL: 'https://hospital-management-uaob.onrender.com/api'
});

export default API;