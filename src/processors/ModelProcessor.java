package processors;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;
import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;

import containers.ModelContainer;
import annotations.CRUD;
import annotations.ColumnName;
import annotations.Model;

@SupportedAnnotationTypes({"annotations.Model", "annotations.CRUD"})
public class ModelProcessor extends AbstractProcessor{

	@Override
	public boolean process(Set<? extends TypeElement> annotations,
			RoundEnvironment roundEnv) {
		
		Set<? extends Element> modelElements = roundEnv.getElementsAnnotatedWith(Model.class);
		for (Element element : modelElements) {
			System.out.println(element.asType());
			
			if (element.getKind() == ElementKind.CLASS) {
				//ModelContainer container = new ModelContainer(element);
				Model m = element.getAnnotation(Model.class);
				String name = m.name();
				
				List<? extends Element> innerElements = element.getEnclosedElements();
				for (Element innerElement : innerElements) {
					if (innerElement.getKind() == ElementKind.FIELD) {
						ColumnName anno = innerElement.getAnnotation(ColumnName.class);
						
						if (anno != null) {
							System.out.println("Successfully found one in the thing" + 
									innerElement.getSimpleName() + " of type: " + innerElement.asType().toString());
						} else {
							System.out.println("Successfully found one not in the thing");
						}
						/*
						List<? extends AnnotationMirror> mirrors = innerElement.getAnnotationMirrors();
						for (AnnotationMirror mirror : mirrors) {
							mirror.getAnnotationType();
						}
						*/
					}
				}
				
				//processingEnv.getMessager().printMessage(Kind.ERROR, "New Error message");
			}
		}
		
		Set<? extends Element> crudElements = roundEnv.getElementsAnnotatedWith(CRUD.class);
		for (Element element : crudElements) {
			
		}

		return false;
	}
	
	@Override
	public SourceVersion getSupportedSourceVersion() {
		return SourceVersion.latestSupported();
	}
	
	@Override
	public Set<String> getSupportedAnnotationTypes() {
		Set<String> set = new HashSet<String>();
		set.add(Model.class.getName());
		return set;
	}
}
