module national_gallery_route_finder {
    requires javafx.controls;
    requires javafx.fxml;


    opens national_gallery_route_finder to javafx.fxml;
    exports national_gallery_route_finder;
}