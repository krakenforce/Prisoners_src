package Application.DAO;

import com.jfoenix.controls.JFXComboBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;

import java.util.List;
import java.util.Set;

public class ComboBox<E> {
    public void LoadCombo(javafx.scene.control.ComboBox<E> comboBox_E, List<E> listE) {
        // TODO Auto-generated method stub

        ObservableList<E> listNoiDen = FXCollections.observableArrayList(listE);

        comboBox_E.setItems(listNoiDen);

        comboBox_E.setCellFactory(new Callback<ListView<E>, ListCell<E>>(){

            @Override
            public ListCell<E> call(ListView<E> p) {

                final ListCell<E> cell = new ListCell<E>(){

                    @Override
                    protected void updateItem(E t, boolean bln) {
                        super.updateItem(t, bln);

                        if(t != null){
                            setText(t.toString());
                        }else{
                            setText(null);
                        }
                    }

                };

                return cell;
            }
        });


        comboBox_E.getSelectionModel().selectFirst();
    }

    public void LoadSetCombo(JFXComboBox<E> comboBox_E, Set<E> listE) {
        ObservableList<E> listNoiDen = FXCollections.observableArrayList(listE);

        comboBox_E.setItems(listNoiDen);

        comboBox_E.setCellFactory(new Callback<ListView<E>, ListCell<E>>(){

            @Override
            public ListCell<E> call(ListView<E> p) {

                final ListCell<E> cell = new ListCell<E>(){

                    @Override
                    protected void updateItem(E t, boolean bln) {
                        super.updateItem(t, bln);

                        if(t != null){
                            setText(t.toString());
                        }else{
                            setText(null);
                        }
                    }

                };

                return cell;
            }
        });

        comboBox_E.getSelectionModel().selectFirst();

    }
}
