package containers;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.type.TypeMirror;

import annotations.CRUD;

public class CrudContainer {
	private Element mElement;
	private TypeMirror mElementType;
	private String mTableName;
	
	public CrudContainer(Element element) {
		if (mElement.getKind() != ElementKind.CLASS) {
			throw new IllegalArgumentException("CRUD annotaiton must be on a class");
		}

		mElement = element;
		
		// This is the element type.
		mElementType = element.asType();
		
		CRUD crud = element.getAnnotation(CRUD.class);
		mTableName = crud.name();
	}

	public String getTableName() {
		return mTableName;
	}
}
