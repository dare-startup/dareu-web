package com.dareu.web.core.security;

public enum SecurityRole {
	ADMIN("ADMIN"), USER("USER"), SPONSOR("SPONSOR"), ALL("ALL");
        
        String value; 
        SecurityRole(String value){
            this.value = value; 
        }
        
        public String getValue(){
            return this.value; 
        }
}
