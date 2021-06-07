//
//  NodeView.swift
//  NICE Algorithms
//
//  Created by NICE on 03/06/2021.
//

import SwiftUI
struct NodeView : View {
    @ObservedObject var model = AlgorithmNetworkModel.model;
    @State var selected_node : Node = Node();
    @State var content : AnyView = AnyView(Text("Click and entity for details"))
    var body : some View {
        
        HStack {
            List {
                ForEach(model.selected_algorithm.nodes, id: \.self){ node in
                    VStack (spacing: 1) {
                        Text("\(node.code):  \(node.text)").bodyFont(size: .XS).lineLimit(1).onTapGesture {
                            selected_node = node
                            content = AnyView(Text("Click and entity for details"))
                        }
                    }.listRowBackground(self.selected_node == node ? Color.standardLightGrey : Color.clear)
                }
            }.frame(width: 300)
            Divider()
            VStack {
                if let node = selected_node {
                    HStack {
                        Text("Database ID").titleFont(size: .S)
                        Spacer()
                        Text("\(node.id)").bodyFont(size: .XS)
                    }
                    HStack {
                        Text("Code").titleFont(size: .S)
                        Spacer()
                        Text("\(node.code)").bodyFont(size: .XS)
                    }
                    HStack {
                        Text("Description").titleFont(size: .S)
                        Spacer()
                        Text("\(node.text)").bodyFont(size: .XS)
                    }
                    HStack {
                        Text("Type").titleFont(size: .S)
                        Spacer()
                        Text("\(node.type)").bodyFont(size: .XS)
                    }
                    HStack {
                        Text("Content Type").titleFont(size: .S)
                        Spacer()
                        Text("\(node.content_type.uppercased()) (\(node.entities.count))").bodyFont(size: .XS)
                    }
                    Divider()
                    VStack(spacing: 2) {
                        Text("Entities")
                        ScrollView (.horizontal) {
                            HStack{
                                ForEach(node.entities, id: \.self) { entity in
                                    EntityCellView(entity: entity, on_click: {
                                        content = EntityHelper.getEntityDetailView(entity: entity)
                                    })
                                }
                            }
                        }
                        Divider()
                        content
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
