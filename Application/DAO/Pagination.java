package Application.DAO;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableView;

import java.util.List;

public class Pagination<S> {
    public Pagination() {

    }
    public void createPagination(javafx.scene.control.Pagination pagination, List<S> listCurent
            , int size, TableView<S> tableView){
        pagination.setPageCount(listCurent.size()/size +1);

        pagination.setPageFactory(pageIndeX ->{

            ObservableList<S> list = FXCollections.observableArrayList(listCurent);

            int fromIndex = pageIndeX * size;
            int toIndex = Math.min(fromIndex + size, list.size());
            tableView.setItems(FXCollections.observableArrayList(list.subList(fromIndex, toIndex)));

            return tableView;

        });
    }
}
