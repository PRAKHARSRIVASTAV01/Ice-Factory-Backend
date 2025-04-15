package com.application.Service;

import com.application.Object.address;
import com.application.Object.order;
import com.application.Object.user_address;
import com.application.Repository.addressRepository;
import com.application.Repository.user_addressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.application.Repository.orderRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class addressService {

    @Autowired
    private addressRepository addressRepository;

    @Autowired
    private user_addressRepository userAddressRepository;

    @Autowired
    private orderRepository orderRepository;

    @Autowired
    private order_statusService orderStatusService; // Make sure this is correctly injected

    public address addAddress(address newAddress) {
        return addressRepository.save(newAddress);
    }

    // public user_address addUserAddress(String phone, Long addressId) {
    //     user_address userAddress = new user_address();
    //     userAddress.setPhone(phone);
    //     userAddress.setAddress_id(addressId);
    //     return userAddressRepository.save(userAddress);
    // }

    public void addUserAddress(String phone, Long addressId) {
        try {
            // First check if a record already exists
            Optional<user_address> existingRecord = userAddressRepository.findById(phone);
            if (existingRecord.isPresent()) {
                // Update existing record
                user_address existing = existingRecord.get();
                existing.setAddress_id(addressId);
                userAddressRepository.save(existing);
                System.out.println("Updated address for user: " + phone);
            } else {
                // Create new record
                user_address newRecord = new user_address(phone, addressId);
                // Double-check the phone is set correctly
                System.out.println("Setting phone to: " + phone + ", phone in object: " + newRecord.getPhone());
                userAddressRepository.save(newRecord);
                System.out.println("Created new user_address record for: " + phone);
            }
        } catch (Exception e) {
            System.err.println("Error in addUserAddress: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    public address getAddressById(Long id) {
        return addressRepository.findById(id).orElse(null);
    }

    public user_address getUserAddressByphone(String phone) {
        return userAddressRepository.findByPhone(phone).orElse(null);
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

    public List<address> getUserAddresses(String phone) {
        List<user_address> userAddresses =  userAddressRepository.findListByPhone(phone);
        return userAddresses.stream()
                .map(user_address::getAddress_id)
                .map(this::getAddressById)
                .collect(Collectors.toList());
    }

    public address getUserAddress(String phone, Long addressId) {
        user_address userAddress = userAddressRepository.findById(phone).orElse(null);
        if (userAddress != null && userAddress.getAddress_id().equals(addressId)) {
            return getAddressById(addressId);
        }
        return null;
    }

    public address updateUserAddress(String phone, Long addressId, address addressDetails) {
        user_address userAddress = userAddressRepository.findById(phone).orElse(null);
        if (userAddress != null && userAddress.getAddress_id().equals(addressId)) {
            return updateAddress(addressId, addressDetails);
        }
        return null;
    }

    public List<order> getOrdersByStatus(String status) {
        // This method should call the non-static method in order_statusService
        List<Long> orderIds = orderStatusService.getIdsByStatus(status);
        List<order> orders = new ArrayList<>();
        for (Long id : orderIds) {
            order o = orderRepository.findById(id).orElse(null);
            if (o != null) {
                orders.add(o);
            }
        }
        return orders;
    }
}
