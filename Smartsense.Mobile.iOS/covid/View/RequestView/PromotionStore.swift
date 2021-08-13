//
//  PromotionStore.swift
//  covid
//
//  Created by SmartSense on 13.08.2021.
//

import Foundation

class PromotionStore: ObservableObject{
    
    var promotionData = [Promotion(id: 1, userID: 1, name: "Gökhan", surname: "Dağtekin", requestStatus: false),
                         Promotion(id: 2, userID: 2, name: "Ahmet", surname: "Dağtekin", requestStatus: false),
                         Promotion(id: 3, userID: 3, name: "Mehmet", surname: "Dağtekin", requestStatus: false),
                         Promotion(id: 4, userID: 4, name: "Arda", surname: "Dağtekin", requestStatus: false),
                         Promotion(id: 5, userID: 5, name: "Azra", surname: "Dağtekin", requestStatus: false)]
    
   @Published var promotions: [Promotion] = []
    /*{
        * Eğer bir kontrol yapılmayacaksa @Publised kullanılabilir
         didSet{
            objectWillChange.send()
        }
    }*/
    
    init() {
        createPromotions()
    }
    
    @discardableResult func createPromotions() -> [Promotion] {
        for index in 0..<10{
            promotions.append(promotionData[index])
        }
        return promotions
    }
    
    func delete(at indexSet: IndexSet) {
        promotions.remove(atOffsets: indexSet)
    }
    
    func move(indices: IndexSet, to newOffSet: Int){
        promotions.move(fromOffsets: indices, toOffset: newOffSet)
    }
    
    func indexSet(for promotion: Promotion) -> IndexSet? {
        if let index = promotions.firstIndex(of: promotion){
            return IndexSet(integer: index)
        }else{
            return nil
        }
    }
    
    func promotion(at indexSet: IndexSet) -> Promotion? {
        if let firstIndex = indexSet.first{
            return promotions[firstIndex]
        }
        return nil
    }
    
    func update(oldValue: Promotion, newValue: Promotion){
        if let index = promotions.firstIndex(of: oldValue){
            promotions[index] =  newValue
        }
    }
}
