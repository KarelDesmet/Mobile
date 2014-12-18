package datevalidator.db;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;
import datevalidator.domain.Category;
import datevalidator.domain.ExpiryProduct;
import datevalidator.domain.Product;
import datevalidator.exception.db.DatabaseException;

public class ExcelExpiryWriter{

	private File file = new File("ExpiryProductDatabaseFinal.xls");
	
	public void write() throws IOException, RowsExceededException,
			WriteException, DatabaseException {
		WritableWorkbook Werkboek = Workbook.createWorkbook(file);
		WritableSheet producten = Werkboek.createSheet("Blad1", 0);
		writeCatalogi(producten);
		Werkboek.write();
		Werkboek.close();
	}

	public void writeCatalogi(WritableSheet tabblad) throws IOException, RowsExceededException, WriteException, DatabaseException {
		Label kolomEan = new Label(0, 0, "EAN");
		Label kolomCategory = new Label(1, 0, "Category");
		Label kolomDay = new Label(2, 0, "Day");
		Label kolomMonth = new Label(3, 0, "Month");
		Label kolomYear = new Label(4, 0, "Year");
		Label kolomSpot = new Label(5, 0, "Spot");
		Label kolomRemoved = new Label(6, 0, "Removed");

		tabblad.addCell(kolomEan);
		tabblad.addCell(kolomCategory);
		tabblad.addCell(kolomDay);
		tabblad.addCell(kolomMonth);
		tabblad.addCell(kolomYear);
		tabblad.addCell(kolomSpot);
		tabblad.addCell(kolomRemoved);

		Map<Category, CategoryExpiryList> expiryProducten = ExpiryList.getInstance().getCategoryExpiryMap();
		
		int r = 1;
		for(Category c: expiryProducten.keySet()){
			CategoryExpiryList cel = ExpiryList.getInstance().getCategoryExpiryList(c);
			for(ExpiryProduct ep : cel.getAllExpiryProducts()){
				Label ean = new Label(0, r, ep.getArticle().getEan().toString());
				Label category = new Label(0, r, ep.getCategory().getName());
				Date date = ep.getExpiryDate();
				int day = date.getDate();
				int month = date.getDate();
				int year = date.getDate();
				Label dayLabel = new Label(0, r, ""+day);
				Label monthLabel = new Label(0, r, ""+month);
				Label yearLabel = new Label(0, r, ""+year);
				Label spot = new Label(0, r, ""+ep.getSpot());
				Label removed = new Label(0, r, ""+ep.isRemoved());
				
				tabblad.addCell(ean);
				tabblad.addCell(category);
				tabblad.addCell(dayLabel);
				tabblad.addCell(monthLabel);
				tabblad.addCell(yearLabel);
				tabblad.addCell(spot);
				tabblad.addCell(removed);
				
				r = r + 1;
			}
		}
		return;
	}
}
