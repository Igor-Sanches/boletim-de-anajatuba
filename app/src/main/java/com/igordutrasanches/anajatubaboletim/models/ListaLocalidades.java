package com.igordutrasanches.anajatubaboletim.models;

import java.util.ArrayList;

public class ListaLocalidades {
        private ArrayList<Localidades> listLocalidades;

        public ArrayList<Localidades> getListLocalidades() {
            return listLocalidades;
        }

    public ListaLocalidades(){
            listLocalidades = new ArrayList<>();
        }

        public String getAvataById(String id){
            for(Localidades friend: listLocalidades){
                if(id.equals(friend.getNome())){
                    return friend.getNome();
                }
            }
            return "";
        }

        public void setListFriend(ArrayList<Localidades> listLocalidades) {
            this.listLocalidades = listLocalidades;
        }
}
