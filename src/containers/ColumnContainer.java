package containers;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Name;
import javax.lang.model.type.TypeMirror;

import constants.Constants;

public class ColumnContainer {
	private Element mInnerElement;
	private Name mElementName;
	private TypeMirror mElementType;
	
	public ColumnContainer(Element innerElement) {
		assert innerElement.getKind() == ElementKind.FIELD;
		
		mInnerElement = innerElement;
		mElementName = innerElement.getSimpleName();
		mElementType = innerElement.asType();
	}
	
	public TypeMirror getElementType() {
		return mElementType;
	}
	
	public String toSQLType() {
		String typeString = mElementType.toString();
		
		if (typeString.equals("java.lang.String")) {
			return Constants.SQL_STRING;
		} else if (typeString.equals("int")) {
			return Constants.SQL_INT;
		} else {
			System.out.println("Error typestring: " + mElementType.toString());
		}
		
		throw new IllegalArgumentException("Unrecognized data type : " + mElementType.toString());
	}
	
	public String getElementName() {
		return mElementName.toString();
	}
}
