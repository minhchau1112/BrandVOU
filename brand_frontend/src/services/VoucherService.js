import axios from 'axios';

const VOUCHER_API_BASE_URL = "http://localhost:8080/api/v1/vouchers";

class VoucherService {

    getVoucher(){
        return axios.get(VOUCHER_API_BASE_URL);
    }

    createVoucher(voucher){
        return axios.post(VOUCHER_API_BASE_URL, voucher);
    }

    getVoucherById(voucherId){
        return axios.get(VOUCHER_API_BASE_URL + '/' + voucherId);
    }

    updateVoucher(voucher, voucherId){
        return axios.put(VOUCHER_API_BASE_URL + '/' + voucherId, voucher);
    }

    deleteVoucher(voucherId){
        return axios.delete(VOUCHER_API_BASE_URL + '/' + voucherId);
    }
}

export default new VoucherService()