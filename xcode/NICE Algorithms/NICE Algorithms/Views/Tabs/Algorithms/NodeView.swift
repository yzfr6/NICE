//
//  NodeView.swift
//  NICE Algorithms
//
//  Created by NICE on 03/06/2021.
//

import SwiftUI
struct NodeView : View {
    @ObservedObject var model = AlgorithmNetworkModel.model;

    @State var selected_entity : Entity = Entity();
    @State var content : AnyView = AnyView(Text("Click an entity for details".uppercased()).bodyFont(size: .XS))
    
    var body : some View {
        
        HStack {
            VStack {
                Title(title: "NODES")
                
                List {

                        ForEach(model.selected_algorithm.nodes, id: \.self){ node in
                            VStack(alignment: .leading) {
                            HStack (spacing: 5) {
                                Text(node.code).titleFont(size: .XS).padding(5).background(Color.secondary).cornerRadius(5)
                                Text("\(node.text)").bodyFont(size: .XXS, color: model.selected_node == node ? Color.red : Color.primary).lineLimit(1).onTapGesture {
                                    model.selected_node = node
                                    content = AnyView(Text("Click an entity for details"))
                                }
                            }
                            HDiv()
                        }.frame(height: 40)
                    }
                }
            }.frame(width: 300)
            
            VDiv()
            VStack {
                if let node = model.selected_node {
                    
                    NodeDetails(node : node)
                    VStack(spacing: 5) {
                        Title(title: "CONTENTS")
                        ScrollView (.horizontal) {
                            HStack{
                                ForEach(node.entities, id: \.self) { entity in
                                    SubTitle(title: "\(entity.type)", color: selected_entity == entity ? Color.green : Color.primary).onTapGesture {
                                        selected_entity = entity;
                                        content = EntityHelper.getEntityDetailView(entity: entity)
                                    }
                                }
                            }.padding(10)
                        }
                        HDiv()
                        if (!selected_entity.isEmpty()){
                            SubTitle(title: selected_entity.type)
                            content
                        }
                        Spacer()
                    }
                    Spacer()
                } else {
                    Text("Select a node").bodyFont(size: .S)
                }
                
            }.makeFullWidth().makeFullHeight()
        }.padding(10)
        
    }
    
}


struct NodeDetails : View {
    
    var node : Node;
    
    var body: some View {
        VStack (spacing: 1) {
            Title(title: "DETAILS")
            Group {
            KeyValue(key: "Database ID", value: "\(node.id)")
            
            HDiv()
            
            KeyValue(key: "Code", value: "\(node.code)")
           
            HDiv()
            
            KeyValue(key: "Description", value: "\(node.text)")
           
            HDiv()
            }
            
            KeyValue(key: "Type", value: "\(node.type)")
            
            HDiv()
            
            KeyValue(key: "Content Type", value: "\(node.content_type.uppercased()) (\(node.entities.count))")
           
            HDiv()
        }
    }
    
}

