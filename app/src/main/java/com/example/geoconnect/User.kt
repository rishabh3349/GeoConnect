package com.example.geoconnect

class User {
    var userName:String?=null
    var age:String?=null
    var currentLatitude: String?=null
    var currentLongitude: String?=null
    var uid:String?=null
    constructor(){}
    constructor(userName:String?,age:String?,currentLatitude:String?,currentLongitude:String?,uid:String?){
        this.userName=userName
        this.age=age
        this.currentLatitude=currentLatitude
        this.currentLongitude=currentLongitude
        this.uid=uid
    }
}