package containers;

import java.util.ArrayList;
import java.util.List;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.type.TypeMirror;

import constants.Constants;
import annotations.Column;
import annotations.Model;

public class ModelContainer {
	private List<ColumnContainer> mColumnContainers;
	private Element mModelElement;
	private String mModelName;
	
	public ModelContainer(Element modelElement) {
		mColumnContainers = new ArrayList<ColumnContainer>();
		mModelElement = modelElement;
		mModelName = modelElement.getAnnotation(Model.class).name();
		
		setColumnNames();
	}
	
	public void setColumnNames() {
		List<? extends Element> innerElements = mModelElement.getEnclosedElements();
		
		for (Element innerElement : innerElements) {
			Column columnAnnotation = innerElement.getAnnotation(Column.class);
			
			// If it isn't a field then something has gone wrong.
			if (columnAnnotation != null && innerElement.getKind() != ElementKind.FIELD) {
				throw new IllegalArgumentException("Can only add ColumnName to fields");
			}
			
			if (columnAnnotation != null) {
				mColumnContainers.add(new ColumnContainer(innerElement));
			}
		}
	}
	
	public String getModelName() {
		Model model = mModelElement.getAnnotation(Model.class);
		return model.name();
	}
	
	public String getTableSQL() {
		StringBuilder sql = new StringBuilder("CREATE TABLE " + mModelName + " (");
		for (int i = 0; i < mColumnContainers.size() - 1; i++) {
			ColumnContainer column = mColumnContainers.get(i);
			String columnName = column.getElementName();
			String columnType = toSQLType(column.getElementType());
			sql.append(columnName + " " + columnType + ", ");
		}
		
		ColumnContainer lastColumn = mColumnContainers.get(mColumnContainers.size() - 1);
		String lastColumnName = lastColumn.getElementName();
		String lastColumnType = toSQLType(lastColumn.getElementType());
		sql.append(lastColumnName + " " + lastColumnType);
		
		sql.append(");");
		return sql.toString();
	}
	
	private String toSQLType(TypeMirror type) {
		String typeString = type.toString();
		
		if (typeString.equals("java.lang.String")) {
			return Constants.SQL_STRING;
		} else if (typeString.equals("int")) {
			return Constants.SQL_INT;
		} else {
			System.out.println("Error typestring: " + type.toString());
		}
		
		throw new IllegalArgumentException("Unrecognized data type : " + type.toString());
	}
}
