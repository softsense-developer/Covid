//
//  CompanionStore.swift
//  covid
//
//  Created by SmartSense on 12.08.2021.
//

import Foundation

class CompanionStore: ObservableObject{
    
    var companionData = [Companion(id: 1, name: "Gökhan", surname: "Dağtekin", email: "gdagtekin23@gmail.com", password: "123456"),
                         Companion(id: 2, name: "Ahmet", surname: "Dağtekin", email: "test1@gmail.com", password: "123456"),
                         Companion(id: 3, name: "Mehmet", surname: "Dağtekin", email: "test2@gmail.com", password: "123456"),
                         Companion(id: 4, name: "Ali", surname: "Dağtekin", email: "test3@gmail.com", password: "123456"),
                         Companion(id: 5, name: "Ayşe", surname: "Dağtekin", email: "test4@gmail.com", password: "123456"),
                         Companion(id: 6, name: "Fatma", surname: "Dağtekin", email: "test5@gmail.com", password: "123456"),
                         Companion(id: 7, name: "Yusuf", surname: "Dağtekin", email: "test6@gmail.com", password: "123456"),
                         Companion(id: 8, name: "Taha", surname: "Dağtekin", email: "test7@gmail.com", password: "123456"),
                         Companion(id: 9, name: "Arda", surname: "Dağtekin", email: "test8@gmail.com", password: "123456"),
                         Companion(id: 10, name: "Azra", surname: "Dağtekin", email: "test9@gmail.com", password: "123456")]
    
   @Published var companions: [Companion] = []
    /*{
        * Eğer bir kontrol yapılmayacaksa @Publised kullanılabilir
         didSet{
            objectWillChange.send()
        }
    }*/
    
    init() {
        createCompanions()
    }
    
    @discardableResult func createCompanions() -> [Companion] {
        for index in 0..<10{
            companions.append(companionData[index])
        }
        return companions
    }
    
    func delete(at indexSet: IndexSet) {
        companions.remove(atOffsets: indexSet)
    }
    
    func move(indices: IndexSet, to newOffSet: Int){
        companions.move(fromOffsets: indices, toOffset: newOffSet)
    }
    
    func indexSet(for companion: Companion) -> IndexSet? {
        if let index = companions.firstIndex(of: companion){
            return IndexSet(integer: index)
        }else{
            return nil
        }
    }
    
    func companion(at indexSet: IndexSet) -> Companion? {
        if let firstIndex = indexSet.first{
            return companions[firstIndex]
        }
        return nil
    }
    
    func update(oldValue: Companion, newValue: Companion){
        if let index = companions.firstIndex(of: oldValue){
            companions[index] =  newValue
        }
    }
}
