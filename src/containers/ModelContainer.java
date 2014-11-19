package containers;

import java.util.ArrayList;
import java.util.List;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;

import annotations.ColumnName;
import annotations.Model;

public class ModelContainer {
	private List<ColumnNameContainer> mColumnNames;
	private Element mModelElement;
	public ModelContainer(Element modelElement) {
		mColumnNames = new ArrayList<ColumnNameContainer>();
		mModelElement = modelElement;
		
		setColumnNames();
	}
	
	public void setColumnNames() {
		List<? extends Element> innerElements = mModelElement.getEnclosedElements();
		
		for (Element innerElement : innerElements) {
			ColumnName columnNameAnnotation = innerElement.getAnnotation(ColumnName.class);
			
			// If it isnt a field then something has gone wrong.
			if (columnNameAnnotation != null && innerElement.getKind() != ElementKind.FIELD) {
				throw new IllegalArgumentException();
			}
			
			if (columnNameAnnotation != null) {
				mColumnNames.add(new ColumnNameContainer(innerElement));
			}
		}
	}
	
	public String getModelName() {
		Model model = mModelElement.getAnnotation(Model.class);
		return model.name();
	}
	
	
}
