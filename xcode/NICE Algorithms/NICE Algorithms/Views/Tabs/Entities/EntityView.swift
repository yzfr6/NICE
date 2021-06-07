//
//  EntityView.swift
//  NICE Algorithms
//
//  Created by NICE on 03/06/2021.
//

import SwiftUI
import SwiftyJSON
import FHIR

struct EntityCellView : View {
    
    
    var entity : Entity
    var on_click : () -> ();
    
    var body: some View {
        Button("\(entity.type)"){
            on_click()
        }
        .padding(20)
        .border(Color.primary)
        .shadow(radius: 5)
        
    }
}


class EntityHelper {

    static func getEntityDetailView(entity : Entity) -> AnyView {
        
        if let resource = entity.observation_decision_1 {
           return  AnyView(ObservationDecision1View(resource: resource))
        } else if let resource = entity.set_goal_1 {
            return AnyView(SetGoal1View(resource: resource))
        } else if let resource = entity.medication_contraindication_decision_1 {
            return AnyView(MedicationContraindicationDecision1View(resource:resource))
        } else if let resource = entity.medication_contraindication_decision_2 {
            return AnyView(MedicationContraindicationDecision2View(resource:resource))
        }  else if let resource = entity.offer_medication_1 {
            return AnyView(OfferMedication1View(resource: resource))
        } else if let resource = entity.set_alert_1 {
            return AnyView(SetAlert1View(resource: resource))
        }else if let resource = entity.set_alert_2 {
            return AnyView(SetAlert2View(resource: resource))
        } else if let resource = entity.condition_decision_1 {
            return AnyView(ConditionDecision1View(resource: resource))
        } else if let resource = entity.stop_medication_1 {
            return AnyView(StopMedication1View(resource: resource))
        } else if let resource = entity.consider_medication_1{
            return AnyView(ConsiderMedication1View(resource: resource))
        } else if let resource = entity.consider_medication_2{
            return AnyView(ConsiderMedication2View(resource: resource))
        } else if let resource = entity.medication_not_tolerated_decision_1{
            return AnyView(MedicationNotTolerated1View(resource: resource))
        }else if let resource = entity.medication_not_tolerated_decision_2{
            return AnyView(MedicationNotTolerated2View(resource: resource))
        } else if let resource = entity.clinician_decision_1 {
            return AnyView(ClinicianDecision1View(resource: resource))
        }
        else {
            return AnyView(Text("Unimplemented"))
        }
    }
    
    
}



struct MedicationContraindicationDecision1View : View {
    
    var contraindications : [MedicinalProductContraindication]
    var medication : Medication
    var generated_string : String
    var resource : MedicationContraindicationDecision1;
    init (resource : MedicationContraindicationDecision1){
        self.resource = resource
        do {
            medication =  try Medication(json: resource.medication.dictionaryObject!)
            generated_string =  "Is \(medication.code?.text?.string ?? "value missing") contraindicated?"
            contraindications = Array()
            
            for contraindication in resource.contraindications {
                let c = try MedicinalProductContraindication(json: contraindication.dictionaryObject!)
                contraindications.append(c)
            }
            
        }
        catch {
            generated_string = "error"
            medication = Medication()
            contraindications = Array()
        }
    }
    
    var body: some View {
        
        ScrollView {
            VStack {
                HStack {
                    Text("Auto generated text")
                    Spacer()
                    Text(generated_string)
                }
                HStack {
                    Text("Medication display")
                    Spacer()
                    Text(medication.code?.text?.string ?? "value missing")
                }
                HStack {
                    Text("Medication system")
                    Spacer()
                    Text(medication.code?.coding?[0].system?.absoluteString ?? "value missing")
                }
                HStack {
                    Text("Medication code")
                    Spacer()
                    Text(medication.code?.coding?[0].code?.string ?? "value missing")
                }
                HStack {
                    Text("Contraindications")
                    Spacer()
                    VStack {
                    ForEach(contraindications.indices){ i in
                        Text("\(contraindications[i].disease?.text?.string ?? "value missing") ")
                    }
                    }
                }
                Divider()
                HStack {
                    Text(resource.medication.rawString() ?? "err")
                    Spacer()
                    VStack {
                        ForEach(resource.contraindications.indices) {i in
                            Text(resource.contraindications[i].rawString() ?? "err")
                        }
                    }
                }
                Spacer()
            }.makeFullHeight()
        }
    }
    
}

struct MedicationContraindicationDecision2View : View {
    
    var medication_code : String
    var generated_string : String
    var resource : MedicationContraindicationDecision2;
    init (resource : MedicationContraindicationDecision2){
        self.resource = resource

            medication_code =  resource.medication_code
            generated_string =  "Is \(medication_code) contraindicated?"
          
    }
    
    var body: some View {
        
        ScrollView {
            VStack {
                HStack {
                    Text("Auto generated text")
                    Spacer()
                    Text(generated_string)
                }
                HStack {
                    Text("Medication code")
                    Spacer()
                    Text(medication_code)
                }
            }.makeFullHeight()
        }
    }
    
}

struct ObservationDecision1View: View {
    
    var observation : Observation
    var generated_string : String
    var resource : ObservationDecision1;
    init (resource : ObservationDecision1){
        self.resource = resource
        do {
            observation =  try Observation(json: resource.observation.dictionaryObject!)
            generated_string =  "Does the patient have a \(observation.code?.text?.string ?? "value missing") reading that is \(observation.valueQuantity?.comparator?.rawValue ?? "?")  \(observation.valueQuantity?.value?.description ?? "0") \(observation.valueQuantity?.unit?.string ?? "unit")"
        }
        catch {
            generated_string = "error"
            observation = Observation()
        }
    }
    
    var body: some View {
      
                    ScrollView {
                    VStack {
                        HStack {
                            Text("Auto generated text")
                            Spacer()
                            Text(generated_string)
                        }
                        HStack {
                            Text("Observation display")
                            Spacer()
                            Text(observation.code?.text?.string ?? "value missing")
                        }
                        HStack {
                            Text("Observation system")
                            Spacer()
                            Text(observation.code?.coding?[0].system?.absoluteString ?? "value missing")
                        }
                        HStack {
                            Text("Observation code")
                            Spacer()
                            Text(observation.code?.coding?[0].code?.string ?? "value missing")
                        }
                        HStack {
                            Text("Expression")
                            Spacer()
                            Text("\(observation.valueQuantity?.comparator?.rawValue ?? "?")  \(observation.valueQuantity?.value?.description ?? "0") \(observation.valueQuantity?.unit?.string ?? "unit") ")
                        }
                        Divider()
                        HStack {
                            Text(resource.observation.rawString() ?? "error")
                            Spacer()
                        }
                        Spacer()
                    }.makeFullHeight()
                    }
    }
}

struct SetGoal1View: View {
    
    var observation : Observation
    var generated_string : String
    var resource : SetGoal1;
    init (resource : SetGoal1){
        self.resource = resource
        do {
            observation =  try Observation(json: resource.observation.dictionaryObject!)
            generated_string =  "Set a target for \(observation.code?.text?.string ?? "value missing") to be \(observation.valueQuantity?.comparator?.rawValue ?? "?")  \(observation.valueQuantity?.value?.description ?? "0") \(observation.valueQuantity?.unit?.string ?? "unit")"
        }
        catch {
            generated_string = "error"
            observation = Observation()
        }
    }
    
    var body: some View {
      
        ScrollView {
        VStack {
            HStack {
                Text("Auto generated text")
                Spacer()
                Text(generated_string)
            }
            HStack {
                Text("Observation display")
                Spacer()
                Text(observation.code?.text?.string ?? "value missing")
            }
            HStack {
                Text("Observation system")
                Spacer()
                Text(observation.code?.coding?[0].system?.absoluteString ?? "value missing")
            }
            HStack {
                Text("Observation code")
                Spacer()
                Text(observation.code?.coding?[0].code?.string ?? "value missing")
            }
            HStack {
                Text("Expression")
                Spacer()
                Text("\(observation.valueQuantity?.comparator?.rawValue ?? "?")  \(observation.valueQuantity?.value?.description ?? "0") \(observation.valueQuantity?.unit?.string ?? "unit") ")
            }
            Divider()
            HStack {
                Text(resource.observation.rawString() ?? "error")
                Spacer()
            }
            Spacer()
        }.makeFullHeight()
        }
    }
}

struct StopMedication1View: View {
    
    var medication : Medication
    var generated_string : String
    var resource : StopMedication1;
    init (resource : StopMedication1){
        self.resource = resource
        do {
            medication =  try Medication(json: resource.medication.dictionaryObject!)
            generated_string =  "Stop \(medication.code?.text?.string ?? "value missing")"
            
        }
        catch {
            generated_string = "error"
            medication = Medication()
        }
    }
    
    var body: some View {
        
        ScrollView {
            VStack {
                HStack {
                    Text("Auto generated text")
                    Spacer()
                    Text(generated_string)
                }
                HStack {
                    Text("Medication display")
                    Spacer()
                    Text(medication.code?.text?.string ?? "value missing")
                }
                HStack {
                    Text("Medication system")
                    Spacer()
                    Text(medication.code?.coding?[0].system?.absoluteString ?? "value missing")
                }
                HStack {
                    Text("Medication code")
                    Spacer()
                    Text(medication.code?.coding?[0].code?.string ?? "value missing")
                }
                
                Divider()
                HStack {
                    Text(resource.medication.rawString() ?? "err")
                }
                Spacer()
            }.makeFullHeight()
        }
    }
}

struct SetAlert1View: View {

    var resource : SetAlert1;
    var generatedString : String = "err";
    var view1 : ObservationDecision1View;
    var view2 : StopMedication1View;
    init (resource : SetAlert1){
        self.resource = resource
        view1 = ObservationDecision1View(resource: resource.observation_decision_1)
        view2 = StopMedication1View(resource: resource.stop_medication_1)
        
        generatedString = "Set alert on (\(view1.generated_string)) : \(view2.generated_string)"
    }

    var body: some View {
        VStack {
            Text(generatedString)
            HStack {
                view1
                Divider()
                view2
            }
        }
       
    }
}


struct SetAlert2View: View {

    var resource : SetAlert2;
    var generatedString : String = "err";
    var view1 : MedicationContraindicationDecision1View;
    var view2 : StopMedication1View;
    init (resource : SetAlert2){
        self.resource = resource
        view1 = MedicationContraindicationDecision1View(resource: resource.medication_contraindication_decision_1)
        view2 = StopMedication1View(resource: resource.stop_medication_1)
        generatedString = "Set alert on (\(view1.generated_string)) : \(view2.generated_string)"
    }

    var body: some View {
        VStack {
            Text(generatedString)
            HStack {
                view1
                Divider()
                view2
            }
        }
    }
}

struct OfferMedication1View: View {
    
    var medication : Medication
    var generated_string : String
    var resource : OfferMedication1;
    init (resource : OfferMedication1){
        self.resource = resource
        do {
            medication =  try Medication(json: resource.medication.dictionaryObject!)
            generated_string =  "Offer \(medication.code?.text?.string ?? "value missing")"
            
        }
        catch {
            generated_string = "error"
            medication = Medication()
        }
    }
    
    var body: some View {
        
        ScrollView {
            VStack {
                HStack {
                    Text("Auto generated text")
                    Spacer()
                    Text(generated_string)
                }
                HStack {
                    Text("Medication display")
                    Spacer()
                    Text(medication.code?.text?.string ?? "value missing")
                }
                HStack {
                    Text("Medication system")
                    Spacer()
                    Text(medication.code?.coding?[0].system?.absoluteString ?? "value missing")
                }
                HStack {
                    Text("Medication code")
                    Spacer()
                    Text(medication.code?.coding?[0].code?.string ?? "value missing")
                }
                
                Divider()
                HStack {
                    Text(resource.medication.rawString() ?? "err")
                }
                Spacer()
            }.makeFullHeight()
        }
    }
}

struct MedicationChoice1View: View {
    
    var medications : [Medication]
    var generated_string : String = ""
    var resource : MedicationChoice1;
    init (resource : MedicationChoice1){
        self.resource = resource
        self.medications = Array()
        
        for m in medications {
            do {
                let medication =  try Medication(json: m.dictionaryObject!)
                medications.append(medication)
            }
            catch {
                
            }
        }
        
        
    }
    
    
    var body: some View {
        
        ScrollView {
            VStack {
                HStack {
                    Text("Auto generated text")
                    Spacer()
                    Text(generated_string)
                }
                HStack {
                    Text("Medication display")
                    Spacer()
                    Text(medication.code?.text?.string ?? "value missing")
                }
                HStack {
                    Text("Medication system")
                    Spacer()
                    Text(medication.code?.coding?[0].system?.absoluteString ?? "value missing")
                }
                HStack {
                    Text("Medication code")
                    Spacer()
                    Text(medication.code?.coding?[0].code?.string ?? "value missing")
                }
                
                Divider()
                HStack {
                    Text(resource.medication.rawString() ?? "err")
                }
                Spacer()
            }.makeFullHeight()
        }
    }
}

struct ConditionDecision1View: View {
    
    var condition : Condition
    var generated_string : String
    var resource : ConditionDecision1;
    init (resource : ConditionDecision1){
        self.resource = resource
        do {
            condition =  try Condition(json: resource.condition.dictionaryObject!)
            generated_string =  "Does the patient have \(condition.code?.text?.string ?? "value missing")?"
        }
        catch {
            print(error.asFHIRError.description)
            generated_string = "error"
            condition = Condition()
        }
    }
    
    var body: some View {
        
        ScrollView {
            VStack {
                HStack {
                    Text("Auto generated text")
                    Spacer()
                    Text(generated_string)
                }
                HStack {
                    Text("Condition display")
                    Spacer()
                    Text(condition.code?.text?.string ?? "value missing")
                }
                HStack {
                    Text("Condition system")
                    Spacer()
                    Text(condition.code?.coding?[0].system?.absoluteString ?? "value missing")
                }
                HStack {
                    Text("Condition code")
                    Spacer()
                    Text(condition.code?.coding?[0].code?.string ?? "value missing")
                }
                
                Divider()
                HStack {
                    Text(resource.condition.rawString() ?? "err")
                }
                Spacer()
            }.makeFullHeight()
        }
    }
}

struct ConsiderMedication1View: View {
    
    var medication : Medication
    var generated_string : String
    var resource : ConsiderMedication1;
    init (resource : ConsiderMedication1){
        self.resource = resource
        do {
            medication =  try Medication(json: resource.medication.dictionaryObject!)
            generated_string =  "Consider \(medication.code?.text?.string ?? "value missing")"
            
        }
        catch {
            generated_string = "error"
            medication = Medication()
        }
    }
    
    var body: some View {
        
        ScrollView {
            VStack {
                HStack {
                    Text("Auto generated text")
                    Spacer()
                    Text(generated_string)
                }
                HStack {
                    Text("Medication display")
                    Spacer()
                    Text(medication.code?.text?.string ?? "value missing")
                }
                HStack {
                    Text("Medication system")
                    Spacer()
                    Text(medication.code?.coding?[0].system?.absoluteString ?? "value missing")
                }
                HStack {
                    Text("Medication code")
                    Spacer()
                    Text(medication.code?.coding?[0].code?.string ?? "value missing")
                }
                
                Divider()
                HStack {
                    Text(resource.medication.rawString() ?? "err")
                }
                Spacer()
            }.makeFullHeight()
        }
    }
}

struct ConsiderMedication2View: View {
    
    var medication_code : String
    var generated_string : String
    var resource : ConsiderMedication2;
    init (resource : ConsiderMedication2){
        self.resource = resource

            medication_code =  resource.medication_code
            generated_string =  "Is \(medication_code) contraindicated?"
          
    }
    
    var body: some View {
        
        ScrollView {
            VStack {
                HStack {
                    Text("Auto generated text")
                    Spacer()
                    Text(generated_string)
                }
                HStack {
                    Text("Medication code")
                    Spacer()
                    Text(medication_code)
                }
            }.makeFullHeight()
        }
    }
}

struct MedicationNotTolerated1View: View {
    
    var medication : Medication
    var generated_string : String
    var resource : MedicationNotToleratedDecision1;
    init (resource : MedicationNotToleratedDecision1){
        self.resource = resource
        do {
            medication =  try Medication(json: resource.medication.dictionaryObject!)
            generated_string =  "Is \(medication.code?.text?.string ?? "value missing") not tolerated?"
            
        }
        catch {
            generated_string = "error"
            medication = Medication()
        }
    }
    
    var body: some View {
        
        ScrollView {
            VStack {
                HStack {
                    Text("Auto generated text")
                    Spacer()
                    Text(generated_string)
                }
                HStack {
                    Text("Medication display")
                    Spacer()
                    Text(medication.code?.text?.string ?? "value missing")
                }
                HStack {
                    Text("Medication system")
                    Spacer()
                    Text(medication.code?.coding?[0].system?.absoluteString ?? "value missing")
                }
                HStack {
                    Text("Medication code")
                    Spacer()
                    Text(medication.code?.coding?[0].code?.string ?? "value missing")
                }
                
                Divider()
                HStack {
                    Text(resource.medication.rawString() ?? "err")
                }
                Spacer()
            }.makeFullHeight()
        }
    }
}

struct MedicationNotTolerated2View: View {
    
    var medication_code : String
    var generated_string : String
    var resource : MedicationNotToleratedDecision2;
    init (resource : MedicationNotToleratedDecision2){
        self.resource = resource
  
        medication_code =  resource.medication_code
            generated_string =  "Is \(medication_code) not tolerated?"
       
    }
    
    var body: some View {
        
        ScrollView {
            VStack {
                HStack {
                    Text("Auto generated text")
                    Spacer()
                    Text(generated_string)
                }
                HStack {
                    Text("Medication code")
                    Spacer()
                    Text(medication_code)
                }
               
            }.makeFullHeight()
        }
    }
}

struct ClinicianDecision1View: View {
    
    //var medication : Medication
    var generated_string : String
    var resource : ClinicianDecision1;
    init (resource : ClinicianDecision1){
        self.resource = resource
        generated_string =  resource.description
        
       
    }
    
    var body: some View {
        
        ScrollView {
            VStack {
                HStack {
                    Text("Generated text")
                    Spacer()
                    Text(generated_string)
                }
            }.makeFullHeight()
        }
    }
}
