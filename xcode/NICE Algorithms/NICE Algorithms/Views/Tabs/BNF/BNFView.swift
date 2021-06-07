//
//  BNFView.swift
//  NICE Algorithms
//
//  Created by NICE on 01/06/2021.
//

import SwiftUI
import AppTools
struct BNFView: View {
    
    var arr = 0...100;
    
    var body: some View {
        NavigationView {
            HStack {
                BNFMasterView()
             
                BNFDetailView()
            }.navigationBarTitle("NICE BNF Viewer", displayMode: .inline)
        }
    }
}

struct BNFMasterView : View {
    
    var arr = 0...100;
    
    var body: some View {
        ScrollView {
            LazyVStack (spacing: 10, pinnedViews: [.sectionHeaders]) {
                Section(header:
                            VStack {
                                Text("DRUGS").titleFont(size: .M)
                            }
                            .makeFullWidth()
                            .padding(5)
                            .background(Color.systemBackground)
                            .addBorder(Color.secondary, width: 1, cornerRadius: 5)
                ){
                    ForEach(arr, id: \.self){ a in
                        VStack (spacing: 1) {
                            Text("\(a)")
                            Divider()
                        }
                    }
                }
            }
            .padding(5)
            .frame(width: 300)
        }.makeFullHeight()
    }
    
}

struct BNFDetailView : View {
    
    @State var selection = 4
    
    var body: some View {
        VStack {
            HStack () {
                Text("Glibeclamide ".uppercased()).titleFont(size: .ML)
                Text(" http://bnf.nice.org.uk/drug/glibenclamide").titleFont(size: .ML, color: Color.secondary)
                Spacer()
            }
            Picker(selection: $selection, label: Text("")) {
                           Text("Side Effects").tag(0)
                           Text("Contraindications").tag(1)
                            Text("FHIR").tag(3)
                           Text("Info").tag(4)
                       }
                       .pickerStyle(SegmentedPickerStyle())
            if selection == 0 {
                
            } else  if selection == 1 {
                
            } else  if selection == 2 {
                
            } else  if selection == 3 {
                
            } else {
                VStack {
                    HStack (alignment: .bottom) {
                        Text("DATABASE ID: ").titleFont(size: .S, color: .secondary)
                        Spacer()
                        Text("00001").titleFont(size: .ML)
                    }
                    HStack (alignment: .bottom) {
                        Text("URI: ").titleFont(size: .S, color: .secondary)
                        Spacer()
                        Text("http://bnf.nice.org.uk/drug/glibenclamide").titleFont(size: .ML)
                    }
                    HStack (alignment: .bottom) {
                        Text("SNOMED CODE: ").titleFont(size: .S, color: .secondary)
                        Spacer()
                        Text("325238000").titleFont(size: .ML)
                    }
                }
            }
            Spacer()
        }
        .padding(10)
        .makeFullHeight()
        .makeFullWidth()
    }
    
}
