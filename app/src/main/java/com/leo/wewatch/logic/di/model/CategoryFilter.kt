package com.leo.wewatch.logic.di.model



enum class CategoryFilter (){
    Entertainment , Movies, Music,Gaming , News , Money , Comedy , Live , New
}

data class filter(val category : CategoryFilter){
   /* fun getCategory():String{
        return category.name
    }*/
}