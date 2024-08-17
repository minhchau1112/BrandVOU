package com.example.brand_backend.controller;

import com.example.brand_backend.exception.ResourceNotFoundException;
import com.example.brand_backend.model.Vouchers;
import com.example.brand_backend.repository.VoucherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/v1/")
public class VoucherController {
    @Autowired
    private VoucherRepository voucherRepository;

    // get all vouchers
    @GetMapping("/vouchers")
    public List<Vouchers> getAllVouchers(){
        return voucherRepository.findAll();
    }

    // create voucher rest api
    @PostMapping("/vouchers")
    public Vouchers createVoucher(@RequestBody Vouchers voucher) {
        return voucherRepository.save(voucher);
    }

    // get voucher by id rest api
    @GetMapping("/vouchers/{id}")
    public ResponseEntity<Vouchers> getVoucherById(@PathVariable int id) {
        Vouchers voucher = voucherRepository.findById((long) id)
                .orElseThrow(() -> new ResourceNotFoundException("Voucher not exist with id :" + id));
        return ResponseEntity.ok(voucher);
    }

    // update voucher rest api
    @PutMapping("/vouchers/{id}")
    public ResponseEntity<Vouchers> updateVoucher(@PathVariable int id, @RequestBody Vouchers voucherDetails){
        Vouchers voucher = voucherRepository.findById((long) id)
                .orElseThrow(() -> new ResourceNotFoundException("Voucher not exist with id :" + id));

        voucher.setCode(voucherDetails.getCode());
        voucher.setImage(voucherDetails.getImage());
        voucher.setDescription(voucherDetails.getDescription());
        voucher.setQRCode(voucherDetails.getQRCode());
        voucher.setDescription(voucherDetails.getDescription());
        voucher.setValue(voucherDetails.getValue());
        voucher.setExpirationDate(voucherDetails.getExpirationDate());
        voucher.setStatus(voucherDetails.getStatus());

        Vouchers updatedVoucher = voucherRepository.save(voucher);
        return ResponseEntity.ok(updatedVoucher);
    }

    // delete voucher rest api
    @DeleteMapping("/voucher/{id}")
    public ResponseEntity<Map<String, Boolean>> deleteVoucher(@PathVariable int id){
        Vouchers voucher = voucherRepository.findById((long) id)
                .orElseThrow(() -> new ResourceNotFoundException("Voucher not exist with id :" + id));

        voucherRepository.delete(voucher);
        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);
        return ResponseEntity.ok(response);
    }
}
