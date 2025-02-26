package com.application.Service;

import com.application.Object.address;
import com.application.Object.user;
import com.application.Object.user_address;
import com.application.Repository.addressRepository;
import com.application.Repository.user_addressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class addressService {

    @Autowired
    private  addressRepository  addressRepository;

    @Autowired
    private user_addressRepository userAddressRepository;


    public address addAddress(address newAddress , String Phone) {
        addUserAddress(Phone, newAddress.getAddress_id());
        return addressRepository.save(newAddress);
    }


    public user_address addUserAddress(String Phone, Long address_id) {
        return userAddressRepository.save(new user_address(Phone, address_id));
    }

    public address getAddressById(Long id) {
        return addressRepository.findById(id).orElse(null);
    }

    public user_address getUserAddressById(String phone) {
        return userAddressRepository.findById(phone).orElse(null);
    }

    public address updateAddress(Long id, address addressDetails) {
        address address = addressRepository.findById(id).orElse(null);
        if (address != null) {
            address.setAddress_id(addressDetails.getAddress_id());
            address.setCity(addressDetails.getCity());
            address.setStreet(addressDetails.getStreet());
            address.setPincode(addressDetails.getPincode());
            address.setStreet(addressDetails.getStreet());
            return addressRepository.save(address);
        }
        return null;
    }


    public boolean deleteAddress(Long id) {
        address address = addressRepository.findById(id).orElse(null);
        if (address != null) {
            addressRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public boolean deleteUserAddress(String phone) {
        user_address userAddress = userAddressRepository.findById(phone).orElse(null);
        if (userAddress != null) {
            userAddressRepository.deleteById(phone);
            return true;
        }
        return false;
    }



}
