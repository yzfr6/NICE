//
//  StepView.swift
//  NICE Algorithms
//
//  Created by NICE on 07/06/2021.
//

import SwiftUI

struct StepView: View {
    
    var node : Node ;
    @State var showingEntityDetails = false;
    @ObservedObject var model = AlgorithmNetworkModel.model
    
    var body: some View {
        VStack {
            HStack {
                Title(title: node.text)
                Title(title: "Show Entities", color: .blue).onTapGesture {
                    showingEntityDetails.toggle()
                }.sheet(isPresented: $showingEntityDetails){
                    NodeEntityContents(node: node);
                }
            }
            HDiv()
            NodeDetails(node:node)
            
            Title(title: "PATHS", color: .blue)
            NodePaths(node: node)
            Spacer()
            
        }.navigationBarTitle("NODE: \(node.code)", displayMode: .inline)
    }
}


struct NodeEntityContents : View {
    var node : Node;
    @State var selected_entity : Entity = Entity()
    @State var content : AnyView = AnyView(Text("Click an entity for details".uppercased()).bodyFont(size: .XS))
    var body: some View {
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
        }.padding(15)
    }
}

struct NodePaths : View {
    
    var node : Node;
    var targets : [(String,Node,Color)]
    init (node n : Node){
        self.node = n
        targets = Array()
        let model = AlgorithmNetworkModel.model
        targets = model.getNodeTargets(node: node);
        
    }
    
    var body: some View {
        ScrollView (.horizontal) {
            HStack{
                ForEach(targets, id: \.self.1) { target in
                    VStack {
                        NavigationLink(destination: StepView(node: target.1)){
                            
                            HStack (alignment: .center) {
                                Text(target.0).titleFont(size: .XS, color: .primary).padding(5).background(target.2).cornerRadius(5)
                                VDiv()
                                Image(systemName: "arrow.right.square").font(Font.system(size: 40)).foregroundColor(target.2)
                                VDiv()
                                Title(title: target.1.text, color: .blue)
                            }.frame(height: 50)
                        }
                        NodeDetails(node: target.1)
                    }.padding(10).addBorder(Color.primary, cornerRadius: 5)
                }
            }
        }
    }
}
