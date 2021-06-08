//
//  EntityView.swift
//  NICE Algorithms
//
//  Created by NICE on 03/06/2021.
//

import SwiftUI
import SwiftyJSON
import FHIR
import AppTools

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
        } else if let resource = entity.medication_choice_1 {
            return AnyView(MedicationChoice1View(resource: resource))
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
                KeyValue(key: "Auto generated text", value: generated_string)
                
                
                KeyValue(key: "Medication display", value: medication.code?.text?.string ?? "value missing")
                
                
                KeyValue(key: "Medication system", value: medication.code?.coding?[0].system?.absoluteString ?? "value missing")
                
                
                KeyValue(key: "Medication code", value: medication.code?.coding?[0].code?.string ?? "value missing")
                
                HStack {
                    Text("Contraindications").entity_content_key()
                    Spacer()
                    VStack {
                        ForEach(contraindications.indices){ i in
                            SmallTitle(title: "\(contraindications[i].disease?.text?.string ?? "value missing")")
                        }
                    }
                }
                
                Divider()
                
                HStack(alignment: .top) {
                    JSONViewer(json: resource.medication.rawString() ?? "err", title: "Medication" ).makeFullWidth()
                
                   
                    VStack {
                        ForEach(resource.contraindications.indices) {i in
                            JSONViewer(json: resource.contraindications[i].rawString() ?? "err", title: "Contraindication").makeFullWidth()
                            
                        }
                    }
                }
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
                KeyValue(key: "Auto generated text", value: generated_string)
                
                KeyValue(key: "Medication code", value: medication_code)
                
                
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
                KeyValue(key: "Auto generated text", value: generated_string)
                
                
                KeyValue(key: "Observation display", value: observation.code?.text?.string ?? "value missing")
                
                
                KeyValue(key: "Observation system", value: observation.code?.coding?[0].system?.absoluteString ?? "value missing")
                
                KeyValue(key: "Observation code", value: observation.code?.coding?[0].code?.string ?? "value missing")
                
                
                KeyValue(key: "Expression", value: "\(observation.valueQuantity?.comparator?.rawValue ?? "?")  \(observation.valueQuantity?.value?.description ?? "0") \(observation.valueQuantity?.unit?.string ?? "unit")")
                
                Divider()
                
                JSONViewer(json: resource.observation.rawString() ?? "err")
                
            }.makeFullHeight()
        }
    }
}

struct SetGoal1View: View {
    
    var goal : Goal
    var generated_string : String
    var resource : SetGoal1;
    init (resource : SetGoal1){
        self.resource = resource
        do {
            goal =  try Goal(json: resource.goal.dictionaryObject!)
            generated_string =  "Set a goal of \(goal.target?[0].measure?.text ?? "value missing") to be \(goal.target?[0].detailQuantity?.comparator?.rawValue ?? "?")  \(goal.target?[0].detailQuantity?.value?.description ?? "0") \(goal.target?[0].detailQuantity?.unit?.string ?? "unit")"
        }
        catch {
            print(error.asFHIRError.description)
            generated_string = "error"
            goal = Goal()
        }
    }
    
    var body: some View {
        
        ScrollView {
            VStack {
                VStack {
                    
                    KeyValue(key: "Auto generated text", value: generated_string)
                    
                    
                    KeyValue(key: "Goal measure display", value: goal.target?[0].measure?.text?.string ?? "value missing")
                    
                    
                    KeyValue(key: "Goal measure system", value: goal.target?[0].measure?.coding?[0].system?.absoluteString ?? "value missing")
                    
                    
                    KeyValue(key: "Goal measure code", value: goal.target?[0].measure?.coding?[0].code?.string ?? "value missing")
                    
                    
                    KeyValue(key: "Goal description", value: goal.description_fhir?.text?.string ?? "err")
                    
                    
                    KeyValue(key: "Goal status", value: goal.lifecycleStatus?.rawValue ?? "value missing")
                    
                    
                    KeyValue(key: "Expression", value: "\(goal.target?[0].detailQuantity?.comparator?.rawValue ?? "?")  \(goal.target?[0].detailQuantity?.value?.description ?? "0") \(goal.target?[0].detailQuantity?.unit?.string ?? "unit")")
                    
                    Divider()
                }
                JSONViewer(json: resource.goal.rawString() ?? "err")
            }.makeFullHeight()
        }
    }
}

struct StopMedication1View: View {
    
    var medication_request : MedicationRequest
    var generated_string : String
    var resource : StopMedication1;
    init (resource : StopMedication1){
        self.resource = resource
        do {
            medication_request =  try MedicationRequest(json: resource.medication_request.dictionaryObject!)
            generated_string =  "Stop \(medication_request.medicationCodeableConcept?.text?.string ?? "value missing")"
            
        }
        catch {
            generated_string = "error"
            medication_request = MedicationRequest()
        }
    }
    
    var body: some View {
        
        ScrollView {
            VStack {
                KeyValue(key: "Auto generated text", value: generated_string)
                
                KeyValue(key: "Medication display", value: medication_request.medicationCodeableConcept?.text?.string ?? "value missing")
                
                
                KeyValue(key: "Medication system", value: medication_request.medicationCodeableConcept?.coding?[0].system?.absoluteString ?? "value missing")
                
                
                KeyValue(key: "Medication code", value: medication_request.medicationCodeableConcept?.coding?[0].code?.string ?? "value missing")
                
                Divider()
                
                JSONViewer(json: resource.medication_request.rawString() ?? "err")
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
                VStack {
                    Text("Obsevation Decision").titleFont(size: .XS, color: Color.green).makeFullWidth().padding(10).background(Color.secondary).cornerRadius(5)
                    view1
                }
                
                Divider()
                
                VStack {
                    Text("Stop Medication").titleFont(size: .XS, color: Color.green).makeFullWidth().padding(10).background(Color.secondary).cornerRadius(5)
                    
                    view2
                }
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
                VStack {
                    Text("Medication Contraindication").titleFont(size: .XS, color: Color.green).makeFullWidth().padding(10).background(Color.secondary).cornerRadius(5)
                    view1
                }
                
                Divider()
                
                VStack {
                    Text("Stop Medication").titleFont(size: .XS, color: Color.green).makeFullWidth().padding(10).background(Color.secondary).cornerRadius(5)
                    view2
                }
            }
        }
    }
}

struct OfferMedication1View: View {
    
    var medication_request : MedicationRequest
    var generated_string : String
    var resource : OfferMedication1;
    init (resource : OfferMedication1){
        self.resource = resource
        do {
            medication_request =  try MedicationRequest(json: resource.medication_request.dictionaryObject!)
            generated_string =  "Consider \(medication_request.medicationCodeableConcept?.text?.string ?? "value missing")"
            
        }
        catch {
            generated_string = "error"
            medication_request = MedicationRequest()
        }
    }
    
    var body: some View {
        
        ScrollView {
            VStack {
                KeyValue(key: "Auto generated text", value: generated_string)
                
                KeyValue(key: "Medication display", value: medication_request.medicationCodeableConcept?.text?.string ?? "value missing")
                
                
                KeyValue(key: "Medication system", value: medication_request.medicationCodeableConcept?.coding?[0].system?.absoluteString ?? "value missing")
                
                
                KeyValue(key: "Medication code", value: medication_request.medicationCodeableConcept?.coding?[0].code?.string ?? "value missing")
                
                Divider()
                
                JSONViewer(json: resource.medication_request.rawString() ?? "err")
                
            }.makeFullHeight()
        }
    }
}

struct MedicationChoice1View: View {
    
    var medications : [Medication]
    var medication_code : String;
    var generated_string : String = ""
    var resource : MedicationChoice1;
    init (resource : MedicationChoice1){
        self.resource = resource
        self.medications = Array()
        self.medication_code = resource.medication_code
        for m in resource.medications {
            do {
                let medication =  try Medication(json:  m.dictionaryObject!)
                medications.append(medication)
            }
            catch {
                
            }
        }
        
        generated_string = "Let \(medication_code) = {\( medications.map({m in m.code?.text?.string ?? "value missing"}).joined(separator: " - "))}"
        
    }
    
    
    var body: some View {
        
        ScrollView {
            VStack {
                HStack {
                    Text("Auto generated text")
                    Spacer()
                    Text(generated_string)
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
                KeyValue(key: "Auto generated text", value: generated_string)
                
                KeyValue(key: "Condition display", value: condition.code?.text?.string ?? "value missing")
                
                
                KeyValue(key: "Condition system", value: condition.code?.coding?[0].system?.absoluteString ?? "value missing")
                
                KeyValue(key: "Condition code", value: condition.code?.coding?[0].code?.string ?? "value missing")
                
                
                Divider()
                JSONViewer(json: resource.condition.rawString() ?? "err")
            }.makeFullHeight()
        }
    }
}

struct ConsiderMedication1View: View {
    
    var medication_request : MedicationRequest
    var generated_string : String
    var resource : ConsiderMedication1;
    init (resource : ConsiderMedication1){
        self.resource = resource
        do {
            medication_request =  try MedicationRequest(json: resource.medication_request.dictionaryObject!)
            generated_string =  "Consider \(medication_request.medicationCodeableConcept?.text?.string ?? "value missing")"
            
        }
        catch {
            generated_string = "error"
            medication_request = MedicationRequest()
        }
    }
    
    var body: some View {
        
        ScrollView {
            VStack {
                
                KeyValue(key: "Auto generated text", value: generated_string)
                
                
                KeyValue(key: "Medication display", value: medication_request.medicationCodeableConcept?.text?.string ?? "value missing")
                
                
                KeyValue(key: "Medication system", value: medication_request.medicationCodeableConcept?.coding?[0].system?.absoluteString ?? "value missing")
                
                
                KeyValue(key: "Medication code", value: medication_request.medicationCodeableConcept?.coding?[0].code?.string ?? "value missing")
                
                
                Divider()
                JSONViewer(json: resource.medication_request.rawString() ?? "err")
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
        generated_string =  "Consider \(medication_code)?"
        
    }
    
    var body: some View {
        
        ScrollView {
            VStack {
                
                KeyValue(key: "Auto generated text", value: generated_string)
                
                KeyValue(key: "Medication code", value: (medication_code))
                
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
                
                KeyValue(key: "Auto generated text", value: generated_string)
                
                
                KeyValue(key: "Medication display", value: medication.code?.text?.string ?? "value missing")
                
                
                KeyValue(key: "Medication system", value: medication.code?.coding?[0].system?.absoluteString ?? "value missing")
                
                
                KeyValue(key: "Medication code", value: medication.code?.coding?[0].code?.string ?? "value missing")
                
                
                Divider()
                JSONViewer(json: resource.medication.rawString() ?? "err")
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
                
                KeyValue(key: "Auto generated text", value: generated_string)
                
                
                KeyValue(key: "Medication code", value: medication_code)
                
                
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
                
                KeyValue(key: "Generated text", value: generated_string)
                
            }.makeFullHeight()
        }
    }
}
