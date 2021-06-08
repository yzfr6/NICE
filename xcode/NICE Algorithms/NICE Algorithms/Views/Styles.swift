//
//  Styles.swift
//  NICE Algorithms
//
//  Created by NICE on 07/06/2021.
//

import SwiftUI
import AppTools
extension Text {
    
    func entity_content_key() -> Text {
        let c = Color.primary
        let ts =  TEXT_SIZE.S
        return self.titleFont(size: ts, color: c)
    }
    
    func entity_content_value() -> Text {
        let c = Color.primary
        let ts =  TEXT_SIZE.S
        return self.bodyFont(size: ts, color: c)
    }
    
}

struct Title : View {
    var title : String
    var color : Color?
    var body: some View {
        Text(title).titleFont(size: .S).makeFullWidth().padding(5).background(color ?? Color.secondary).cornerRadius(5)
    }
}

struct SubTitle : View {
    var title : String
    var color : Color?
    var body: some View {
        Text(title).subTitleFont(size: .XS, color: color ?? Color.green).makeFullWidth().padding(15).background(Color.secondary).cornerRadius(5)
    }
}

struct SmallTitle : View {
    var title : String
    var color : Color?
    var body: some View {
        Text(title).bodyFont(size: .XS, color: color ?? Color.green).padding(10).background(Color.secondary).cornerRadius(5)
    }
}


struct KeyValue : View {
    var key : String;
    var value : String;
    
    var body: some View {
        HStack {
            Text(key).entity_content_key()
            Spacer()
            Text(value).entity_content_value()
        }
    }
}

struct JSONViewer : View {
    var json : String;
    var title : String?
    var body: some View {
        HStack {
            VStack (alignment: .leading) {
                SubTitle(title: title ?? "FHIR-JSON")
                Text(json).bodyFont(size: .XXS).lineLimit(nil)
            }
        }
    }
}

struct VDiv: View {
    
    var backgroundColor : Color = .primary;
    let width : CGFloat? = 1
    var body: some View {
       
        Divider().frame(width: width).background(LinearGradient(gradient: .init(colors: [.clear, backgroundColor, .clear]), startPoint: .top, endPoint: .bottom))
    }
}


struct HDiv: View {
    
    var backgroundColor : Color = .primary;
    let height : CGFloat? = 1
    var body: some View {
       
        Divider().frame(height: height).background(LinearGradient(gradient: .init(colors: [.clear, backgroundColor, .clear]), startPoint: .leading, endPoint: .trailing))
    }
}
