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
			String columnType = column.toSQLType();
			sql.append(columnName + " " + columnType + ", ");
		}
		
		ColumnContainer lastColumn = mColumnContainers.get(mColumnContainers.size() - 1);
		String lastColumnName = lastColumn.getElementName();
		String lastColumnType = lastColumn.toSQLType();
		sql.append(lastColumnName + " " + lastColumnType);
		
		sql.append(");");
		return sql.toString();
	}

	public String getCreateQuery() {
		StringBuilder sql = new StringBuilder("INSERT INTO " + mModelName + " VALUES (");
		
		for (int i = 0; i < mColumnContainers.size() - 1; i++) {
			ColumnContainer column = mColumnContainers.get(i);
			String columnType = column.toSQLType();
			sql.append(getToken(columnType) + ", ");
		}
		
		ColumnContainer lastColumn = mColumnContainers.get(mColumnContainers.size() - 1);
		String lastColumnType = lastColumn.toSQLType();
		sql.append(getToken(lastColumnType));
		
		sql.append(");");
		
		return sql.toString();
	}
	
	private String getToken(String type) {
		if (type.equals(Constants.SQL_STRING)) {
			return "'%s'";
		} else if (type.equals(Constants.SQL_INT)) {
			return "%d";
		}
		throw new IllegalArgumentException("Unrecognized data type : " + type);
	}
	
	public String getUpdateQuery() {
		/*
		UPDATE table_name
		SET column1=value1,column2=value2,...
		WHERE some_column=some_value;
		*/
		
		StringBuilder sql = new StringBuilder("UPDATE " + mModelName + " SET ");
		
		for (int i = 0; i < mColumnContainers.size() - 1; i++) {
			ColumnContainer column = mColumnContainers.get(i);
			String columnType = column.toSQLType();
			String columnName = column.getElementName();
			sql.append(columnName + "=" + getToken(columnType) + ", ");
		}
		
		ColumnContainer lastColumn = mColumnContainers.get(mColumnContainers.size() - 1);
		String lastColumnName = lastColumn.getElementName();
		
		String lastColumnType = lastColumn.toSQLType();
		
		sql.append(String.format("%s = %s ", lastColumnName, getToken(lastColumnType)));
		
		sql.append("WHERE id = %d;");
		return sql.toString();
	}
	
	public String getDeleteQuery() {
		return "DELETE FROM " + mModelName + " WHERE id=%d;";
	}
	
	public String getIndexQuery() {
		return String.format("SELECT * FROM %s;", mModelName);
	}
	
	public String getFindQuery() {
		return "SELECT * FROM " + mModelName + " WHERE id=%d;";
	}
}
