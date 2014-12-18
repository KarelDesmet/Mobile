package datevalidator.db;

import java.io.File;
import java.io.IOException;
import java.util.List;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;
import datevalidator.domain.Product;

public class ExcelWriter{

	private File file = new File("ProductDatabaseFinal.xls");
	
	public void write() throws IOException, RowsExceededException,
			WriteException {
		WritableWorkbook Werkboek = Workbook.createWorkbook(file);
		WritableSheet producten = Werkboek.createSheet("Blad1", 0);
		writeCatalogi(producten);
		Werkboek.write();
		Werkboek.close();
	}

	public void writeCatalogi(WritableSheet tabblad) throws IOException, RowsExceededException, WriteException {
		List<Product> producten = Database.getProducten();
		Label kolomEan = new Label(0, 0, "EAN");
		Label kolomNaam = new Label(1, 0, "Name");
		Label kolomHope = new Label(2, 0, "HOPE");
		Label kolomCategory = new Label(3, 0, "Category");

		tabblad.addCell(kolomEan);
		tabblad.addCell(kolomNaam);
		tabblad.addCell(kolomHope);
		tabblad.addCell(kolomCategory);

		int r = 1;
		for (Product pr : producten) {
			Label ean = new Label(0, r, pr.getEan().toString());
			Label name = new Label(1, r, pr.getName());
			Label hope = new Label(2, r, pr.getHopeString());
			Label category = new Label(3, r, pr.getCategory().toString());
			
			tabblad.addCell(ean);
			tabblad.addCell(name);
			tabblad.addCell(hope);
			tabblad.addCell(category);
			
			r = r + 1;
			}
		return;
	}
}
