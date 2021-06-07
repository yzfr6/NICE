//
//  Datastructes.swift
//  NICE Algorithms
//
//  Created by NICE on 02/06/2021.
//

import Foundation
import FHIR
import SwiftUI
import SwiftyJSON

struct Algorithm : Encodable, Decodable, Hashable  {
    static func == (lhs: Algorithm, rhs: Algorithm) -> Bool {
        return lhs.id == rhs.id
    }
    
    func hash(into hasher: inout Hasher) {
        hasher.combine(id)
    }
    
    var id : Int = -1;
    var name : String = "Select Algorithm";
    var nodes : [Node] = Array();
    var edges : [Edge] = Array ();
    
    func isEmpty() -> Bool {
        return id == -1
    }
    
}

struct ClinicianDecision1 : Codable  {
    
   // var id : Int = 0;
    var description : String = "";
    
}

struct ConditionDecision1 : Codable  {
    
   // var id : Int = 0 ;
    var condition : JSON = [:]
    
}


struct ConsiderMedication1 : Codable  {
    
    //var id : Int = 0;
    var medication : JSON = [:]
    
}


struct ConsiderMedication2 : Codable {
    
    //var id : Int = 0;
    var medication_code : String = ""
    
}

struct Edge: Encodable, Decodable  {
    var id : Int = 0
    var source_id : Int = 0
    var target_id : Int = 0
    var type : Int = 0;
}


struct Entity : Encodable, Decodable, Hashable  {
    static func == (lhs: Entity, rhs: Entity) -> Bool {
        return lhs.entity_id == rhs.entity_id
    }
    
    func hash(into hasher: inout Hasher) {
        hasher.combine(entity_id)
    }
    
    var entity_id : Int = 0;
    var type : String = ""
    
    var clinician_decision_1 : ClinicianDecision1?// = ClinicianDecision1()
    var condition_decision_1 : ConditionDecision1?// = ConditionDecision1()
    var consider_medication_1 : ConsiderMedication1?// = ConsiderMedication1()
    var consider_medication_2 : ConsiderMedication2?// = ConsiderMedication2()
    var medication_choice_1 : MedicationChoice1?// = MedicationChoice1()
    var medication_contraindication_decision_1: MedicationContraindicationDecision1?// = MedicationContraindicationDecision1()
    var medication_contraindication_decision_2: MedicationContraindicationDecision2?// = MedicationContraindicationDecision2()
    var medication_not_tolerated_decision_1 : MedicationNotToleratedDecision1?// = MedicationNotToleratedDecision1()
    var medication_not_tolerated_decision_2 : MedicationNotToleratedDecision2?// = MedicationNotToleratedDecision2()
    var observation_decision_1 : ObservationDecision1?// = ObservationDecision1()
    var offer_medication_1 : OfferMedication1?// = OfferMedication1()
    var set_alert_1 : SetAlert1?// = SetAlert1()
    var set_alert_2 : SetAlert2?// = SetAlert2()
    var set_goal_1 : SetGoal1?// = SetGoal1()
    var stop_medication_1 : StopMedication1?// = StopMedication1()
    
}
/*
enum EntityType : String {
    case CLINICIAN_DECISION_1 = "algorithm_clinician_decision_1"
    case     CONDITION_DECISION_1 = "algorithm_condition_decision_1"
    case      CONSIDER_MEDICATION_1 = "algorithm_consider_medication_1"
    case        CONSIDER_MEDICATION_2 = "algorithm_consider_medication_2"
    case        MEDICATION_CHOICE_1 = "algorithm_medication_choice_1"
    case         MEDICATION_CONTRAINDICATION_DECISION_1 = "algorithm_medication_contraindication_decision_1"
    case         MEDICATION_CONTRAINDICATION_DECISION_2 = "algorithm_medication_contraindication_decision_2"
    case         MEDICATION_NOT_TOLERATED_1 = "algorithm_medication_not_tolerated_decision_1"
    case         MEDICATION_NOT_TOLERATED_2 = "algorithm_medication_not_tolerated_decision_2"
    case         OBSERVATION_DECISION_1 = "algorithm_observation_decision_1"
    case       OFFER_MEDICATION_1 = "algorithm_offer_medication_1"
    case         SET_ALERT_1 = "algorithm_set_alert_1"
    case         SET_ALERT_2 = "algorithm_set_alert_2"
    case         SET_GOAL_1 = "algorithm_set_goal_1"
    case         STOP_MEDICATION_1 = "algorithm_stop_medication_1"
}*/

struct MedicationChoice1  : Codable{
    //var id : Int = 0
    var medication_code : String = ""
    var medications : [JSON] = Array()
    
}

struct MedicationContraindicationDecision1 : Codable {
    //var id : Int = 0
    var medication : JSON = [:]
    var contraindications : [JSON] = Array()
    
}

struct MedicationContraindicationDecision2 : Codable  {
   // var id : Int = 0;
    var medication_code : String = ""
}

struct MedicationNotToleratedDecision1 : Codable  {
    //var id : Int = 0
    var medication : JSON = [:]
}

struct MedicationNotToleratedDecision2 : Codable {
    //var id : Int = 0;
    var medication_code : String = ""
}

struct Node  :  Codable, Hashable {
    static func == (lhs: Node, rhs: Node) -> Bool {
        return lhs.id == rhs.id
    }
    
    func hash(into hasher: inout Hasher) {
        hasher.combine(id)
    }
    
    var id : Int = 0
    var code : String = ""
    var text : String = ""
    var type : String = ""
    var content_type : String = ""
    var entities : [Entity] = Array()
    
    enum CodingKeys: CodingKey {
        case id, code, text, type, content_type, entities
    }
    
    /*init(from decoder: Decoder) throws {
        let container =  try decoder.container (keyedBy: CodingKeys.self)
        id = try container.decode (Int.self, forKey: .id)
        code = try container.decode (String.self, forKey: .code)
        text = try container.decode (String.self, forKey: .text)
        type = try container.decode (String.self, forKey: .type)
        content_type = try container.decode (String.self, forKey: .content_type)
        
        let rawEntities : [Entity] = try container.decode (Array<Entity>.self, forKey: .entities)
        entities = Array()
        
        for entity in rawEntities {
            switch entity.type {
            case EntityType.CLINICIAN_DECISION_1.rawValue:
                entities.append(try ClinicianDecision1(from: decoder))
            case     EntityType.CONDITION_DECISION_1.rawValue:
                entities.append(try ConditionDecision1(from: decoder))
            case     EntityType.CONSIDER_MEDICATION_1.rawValue:
                entities.append(try ConsiderMedication1(from: decoder))
            case       EntityType.CONSIDER_MEDICATION_2.rawValue:
                entities.append(try ConsiderMedication2(from: decoder))
            case       EntityType.MEDICATION_CHOICE_1.rawValue:
                entities.append(try MedicationChoice1(from: decoder))
            case       EntityType.MEDICATION_CONTRAINDICATION_DECISION_1.rawValue:
                entities.append(try MedicationContraindicationDecision1(from: decoder))
            case       EntityType.MEDICATION_CONTRAINDICATION_DECISION_2.rawValue:
                entities.append(try MedicationContraindicationDecision2(from: decoder))
            case       EntityType.MEDICATION_NOT_TOLERATED_1.rawValue:
                entities.append(try MedicationNotToleratedDecision1(from: decoder))
            case       EntityType.MEDICATION_NOT_TOLERATED_2.rawValue:
                entities.append(try MedicationNotToleratedDecision2(from: decoder))
            case       EntityType.OBSERVATION_DECISION_1.rawValue:
                entities.append(try ObservationDecision1(from: decoder))
            case      EntityType.OFFER_MEDICATION_1.rawValue:
                entities.append(try OfferMedication1(from: decoder))
            case       EntityType.SET_ALERT_1.rawValue:
                entities.append(try SetAlert1(from: decoder))
            case       EntityType.SET_ALERT_2.rawValue:
                entities.append(try SetAlert2(from: decoder))
            case       EntityType.SET_GOAL_1.rawValue:
                entities.append(try SetGoal1(from: decoder))
            case       EntityType.STOP_MEDICATION_1.rawValue:
                entities.append(try StopMedication1(from: decoder))
            default:
                entities.append(entity)
            }
        }
    }*/
    
}

struct ObservationDecision1 : Codable {
    //var id : Int = 0
    var observation : JSON = [:]
}

struct OfferMedication1 : Codable {
    //var id : Int = 0
    var medication :JSON = [:]
}

struct SetAlert1  : Codable  {
    //var id : Int = 0
    var observation_decision_1 : ObservationDecision1 = ObservationDecision1()
    var stop_medication_1 : StopMedication1 = StopMedication1()
}

struct SetAlert2  : Codable {
   // var id : Int = 0
    var medication_contraindication_decision_1 : MedicationContraindicationDecision1 = MedicationContraindicationDecision1()
    var stop_medication_1 : StopMedication1 = StopMedication1()
}

struct SetGoal1 : Codable {
    //var id : Int = 0;
    var observation : JSON = [:]
}

struct StopMedication1 : Codable {
    //var id : Int = 0
    var medication : JSON = [:]
}
