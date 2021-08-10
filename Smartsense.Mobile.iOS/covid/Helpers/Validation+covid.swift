//
//  Validation+covid.swift
//  covid
//
//  Created by SmartSense on 2.06.2021.
//

import Foundation


class Validation{
    public func validateString(string: String, maxLength: Int) ->Bool {
        // Length be 18 characters max and 2 characters minimum
        let stringRegex = "^\\w{2," + String(maxLength) + "}$"
        let trimmedString = string.trimmingCharacters(in: .whitespaces)
        let validateString = NSPredicate(format: "SELF MATCHES %@", stringRegex)
        let isValidateString = validateString.evaluate(with: trimmedString)
        return isValidateString
    }
    
    public func validateName(name: String) ->Bool {
        // Length be 18 characters max and 2 characters minimum
        let nameRegex = "^\\w{2,18}$"
        let trimmedString = name.trimmingCharacters(in: .whitespaces)
        let validateName = NSPredicate(format: "SELF MATCHES %@", nameRegex)
        let isValidateName = validateName.evaluate(with: trimmedString)
        return isValidateName
    }
    
    public func validateTCIdentity(tc: String) ->Bool {
        // Length be 11 characters
        let nameRegex = "^[0-9]{11}$"
        let trimmedString = tc.trimmingCharacters(in: .whitespaces)
        let validateTCIdentity = NSPredicate(format: "SELF MATCHES %@", nameRegex)
        let isValidateTCIdentity = validateTCIdentity.evaluate(with: trimmedString)
        return isValidateTCIdentity
    }
    
    public func validaPhoneNumber(phoneNumber: String) -> Bool {
        let phoneNumberRegex = "^[6-9]\\d{9}$"
        let trimmedString = phoneNumber.trimmingCharacters(in: .whitespaces)
        let validatePhone = NSPredicate(format: "SELF MATCHES %@", phoneNumberRegex)
        let isValidPhone = validatePhone.evaluate(with: trimmedString)
        return isValidPhone
    }
    public func validateEmail(email: String) -> Bool {
        let emailRegEx = "[A-Z0-9a-z._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,64}"
        let trimmedString = email.trimmingCharacters(in: .whitespaces)
        let validateEmail = NSPredicate(format:"SELF MATCHES %@", emailRegEx)
        let isValidateEmail = validateEmail.evaluate(with: trimmedString)
        return isValidateEmail
    }
    public func validatePassword(password: String) -> Bool {
        // Length be 18 characters max and 6 characters minimum
        let passRegEx = "^\\w{6,18}$"
        let trimmedString = password.trimmingCharacters(in: .whitespaces)
        let validatePassord = NSPredicate(format:"SELF MATCHES %@", passRegEx)
        let isvalidatePass = validatePassord.evaluate(with: trimmedString)
        return isvalidatePass
    }
    /*public func validatePassword(password: String) -> Bool {
     //Minimum 8 characters at least 1 Alphabet and 1 Number:
     let passRegEx = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$"
     let trimmedString = password.trimmingCharacters(in: .whitespaces)
     let validatePassord = NSPredicate(format:"SELF MATCHES %@", passRegEx)
     let isvalidatePass = validatePassord.evaluate(with: trimmedString)
     return isvalidatePass
     }*/
    public func validateAnyOtherTextField(otherField: String) -> Bool {
        let otherRegexString = "Your regex String"
        let trimmedString = otherField.trimmingCharacters(in: .whitespaces)
        let validateOtherString = NSPredicate(format: "SELF MATCHES %@", otherRegexString)
        let isValidateOtherString = validateOtherString.evaluate(with: trimmedString)
        return isValidateOtherString
    }
}

struct Validator {
    
    func textFieldValidatorEmail(_ string: String) -> Bool {
        if string.count > 100 {
            return false
        }
        let emailFormat = "(?:[\\p{L}0-9!#$%\\&'*+/=?\\^_`{|}~-]+(?:\\.[\\p{L}0-9!#$%\\&'*+/=?\\^_`{|}" + "~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\" + "x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[\\p{L}0-9](?:[a-" + "z0-9-]*[\\p{L}0-9])?\\.)+[\\p{L}0-9](?:[\\p{L}0-9-]*[\\p{L}0-9])?|\\[(?:(?:25[0-5" + "]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-" + "9][0-9]?|[\\p{L}0-9-]*[\\p{L}0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21" + "-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])"
        let emailPredicate = NSPredicate(format:"SELF MATCHES %@", emailFormat)
        return emailPredicate.evaluate(with: string)
    }
}


