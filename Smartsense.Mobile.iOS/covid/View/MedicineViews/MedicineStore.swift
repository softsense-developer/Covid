//
//  MedicineStore.swift
//  covid
//
//  Created by SmartSense on 12.08.2021.
//

import Foundation

class MedicineStore: ObservableObject{
    
    var medicineData = [Medicine(id: "a1", medicineName: "Aspirin", onComingUsageTime: Date().addingTimeInterval(-60)),
                        Medicine(id: "a2", medicineName: "Karbamazepin", onComingUsageTime: Date().addingTimeInterval(-120)),
                        Medicine(id: "a3", medicineName: "Levatirasetam", onComingUsageTime: Date().addingTimeInterval(-180)),
                        Medicine(id: "a4", medicineName: "Okskarbamazepin", onComingUsageTime: Date().addingTimeInterval(-240)),
                        Medicine(id: "a5", medicineName: "Lamotrijin", onComingUsageTime: Date().addingTimeInterval(-300)),
                        Medicine(id: "a6", medicineName: "Topiramat", onComingUsageTime: Date().addingTimeInterval(-360)),
                        Medicine(id: "a7", medicineName: "Fenitoin", onComingUsageTime: Date().addingTimeInterval(-420)),
                        Medicine(id: "a8", medicineName: "Fenobarbital", onComingUsageTime: Date().addingTimeInterval(-480))]
    
   @Published var medicines: [Medicine] = []
    /*{
        * Eğer bir kontrol yapılmayacaksa @Publised kullanılabilir
         didSet{
            objectWillChange.send()
        }
    }*/
    
    init() {
        createMedicines()
    }
    
    @discardableResult func createMedicines() -> [Medicine] {
        for index in 0..<8{
            medicines.append(medicineData[index])
        }
        return medicines
    }
    
    func delete(at indexSet: IndexSet) {
        medicines.remove(atOffsets: indexSet)
    }
    
    func move(indices: IndexSet, to newOffSet: Int){
        medicines.move(fromOffsets: indices, toOffset: newOffSet)
    }
    
    func indexSet(for medicine: Medicine) -> IndexSet? {
        if let index = medicines.firstIndex(of: medicine){
            return IndexSet(integer: index)
        }else{
            return nil
        }
    }
    
    func medicine(at indexSet: IndexSet) -> Medicine? {
        if let firstIndex = indexSet.first{
            return medicines[firstIndex]
        }
        return nil
    }
    
    func update(oldValue: Medicine, newValue: Medicine){
        if let index = medicines.firstIndex(of: oldValue){
            medicines[index] =  newValue
        }
    }
}
