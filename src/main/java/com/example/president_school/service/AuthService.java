//package com.example.president_school.service;
//
//import com.example.president_school.repository.EmployeeRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.stereotype.Service;
//
//@Service
//@RequiredArgsConstructor
//public class AuthService implements UserDetailsService{
//    private final EmployeeRepository employeeRepository;
//
//    @Override
//    public UserDetails loadUserByUsername(String phone) throws UsernameNotFoundException {
//        return employeeRepository.findByPhone(phone)
//                .orElseThrow(() -> new UsernameNotFoundException(String.format("%s raqamli foydalanuvchi topilmadi", phone)));
//    }
//}
