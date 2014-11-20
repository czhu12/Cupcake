package containers;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Name;
import javax.lang.model.type.TypeMirror;

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
	
	public String getElementName() {
		return mElementName.toString();
	}
}
