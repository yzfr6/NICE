//
//  NetworkModel.swift
//  OFR
//
//  Created by Elliot Alderson on 11/11/2020.
//

import Foundation
import CoreData

class NetworkModel : ObservableObject {

    @Published var nlt = NetworkLoaderTracker();
    @Published var isLoading = false;
    let timeoutInterval : Double = 20;
    func refresh() {
        increment()
        decrement()
    }
    
    func increment(){
        isLoading = true;
        nlt.increment()
        self.objectWillChange.send()
    }
    
    func decrement(){
        isLoading = nlt.decrement()
        self.objectWillChange.send()
    }
    
    func pushUpdate() {
        self.objectWillChange.send()
    }
    
}

