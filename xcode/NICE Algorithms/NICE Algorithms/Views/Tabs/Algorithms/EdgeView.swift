//
//  EdgeView.swift
//  NICE Algorithms
//
//  Created by NICE on 08/06/2021.
//

import SwiftUI

struct EdgeView: View {
    
    @ObservedObject var model = AlgorithmNetworkModel.model
    
    var body: some View {
        List {
            ForEach(model.selected_algorithm.edges, id: \.self){edge in
                let x = model.getEdgeType(edge: edge)
                HStack (alignment: .center) {
                    Text(edge.source.code).titleFont(size: .XS).padding(5).background(Color.secondary).cornerRadius(5)
                    Title(title: edge.source.text, color: .blue)
                    VDiv()
                    Image(systemName: "arrow.right.square").font(Font.system(size: 40)).foregroundColor(x.1)
                    VDiv()
                    Title(title: edge.target.text, color: .blue)
                    Text(edge.target.code).titleFont(size: .XS).padding(5).background(Color.secondary).cornerRadius(5)
                }.frame(height: 50)
            }
        }
    }
}

