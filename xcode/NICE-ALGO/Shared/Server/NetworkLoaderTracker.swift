//
//  NetworkLoaderModel.swift
//  OFR
//
//  Created by Elliot Alderson on 06/11/2020.
//

import Foundation
import SwiftUI

class NetworkLoaderTracker {
    
    @Published var isLoading : Bool = false;
    
    private var loadingCount : Int = 0;
    
    func decrement() -> Bool{
        loadingCount -= 1;
        if loadingCount < 0 {
            loadingCount = 0;
        }
        return loadingCount > 0
        
    }
    
    func increment(){
        self.loadingCount += 1;
        
    }
}
