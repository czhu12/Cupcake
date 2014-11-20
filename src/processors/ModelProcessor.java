package processors;

import java.io.StringWriter;
import java.util.ArrayList;
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
import javax.tools.JavaFileObject;

import com.squareup.javawriter.JavaWriter;

import containers.CrudContainer;
import containers.ModelContainer;
import annotations.CRUD;
import annotations.Column;
import annotations.Model;

@SupportedAnnotationTypes({"annotations.Model", "annotations.CRUD"})
public class ModelProcessor extends AbstractProcessor{

	@Override
	public boolean process(Set<? extends TypeElement> annotations,
			RoundEnvironment roundEnv) {
		List<ModelContainer> modelContainers = new ArrayList<ModelContainer>();
		List<CrudContainer> crudContainers = new ArrayList<CrudContainer>();
		
		// Here we iterate through all models.
		Set<? extends Element> modelElements = roundEnv.getElementsAnnotatedWith(Model.class);
		
		for (Element element : modelElements) {
			System.out.println(element.asType());
			
			if (element.getKind() == ElementKind.CLASS) {
				ModelContainer modelContainer = new ModelContainer(element);
				modelContainers.add(modelContainer);
				//processingEnv.getMessager().printMessage(Kind.ERROR, "New Error message");
			}
		}
		
		// Here we iterate through all model interfaces.
		Set<? extends Element> crudElements = roundEnv.getElementsAnnotatedWith(CRUD.class);
		for (Element element : crudElements) {
			CrudContainer crudContainer = new CrudContainer(element);
			crudContainers.add(crudContainer);
		}
		
		StringBuilder schema = new StringBuilder();
		for (ModelContainer container : modelContainers) {
			schema.append(container.getTableSQL());
		}
		
		System.out.println(schema.toString());
		
		StringWriter stringWriter = new StringWriter();
		
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
