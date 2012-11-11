package by.chuger.cookbook.view.bean;

import by.chuger.cookbook.model.dao.Facade;
import by.chuger.cookbook.model.domain.Recipe;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.inject.Named;
import java.text.DecimalFormat;

@Component
@Named("ratingMB")
@Scope("request")
public class RatingMB {

    @Autowired
    private Facade facade;

    public RatingMB() {
    }

    public Double rating(final Recipe recipe) {
        return facade.getAvgMarkByRecipe(recipe);
    }

    public String ratingString(final Recipe recipe) {
        if (recipe != null) {
            return DoubleToString(facade.getAvgMarkByRecipe(recipe));
        }
        return null;
    }

    private String DoubleToString(final Double doubleValue) {
        try {
            return new DecimalFormat("#.##").format(doubleValue);
        } catch (NullPointerException e) {
        } catch (IllegalArgumentException e) {
        }
        return null;
    }

}
