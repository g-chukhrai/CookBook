package by.chuger.cookbook.model.dao;

import by.chuger.cookbook.model.domain.*;
import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

public class FacadeImpl extends HibernateDaoSupport implements Facade {

    @Autowired
    private BaseDAO<Category> categoryDAO;
    @Autowired
    private BaseDAO<Recipe> recipeDAO;
    @Autowired
    private BaseDAO<Amount> amountDAO;
    @Autowired
    private BaseDAO<Ingridient> ingridientDAO;
    @Autowired
    private BaseDAO<Product> productDAO;
    @Autowired
    private BaseDAO<UserAccount> userAccountDAO;
    @Autowired
    private BaseDAO<UserAuthority> userAuthorityDAO;
    @Autowired
    private BaseDAO<RecipeMark> recipeMarkDAO;
    @Autowired
    private BaseDAO<Mark> markDAO;
    @Autowired
    private BaseDAO<Feedback> feedbackDAO;

    public FacadeImpl() {
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void createCategory(final Category entity) {
        categoryDAO.create(entity);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void createRecipe(final Recipe recipe) {
        recipeDAO.create(recipe);
        for (Product product : recipe.getProducts()) {
            Amount amount = product.getAmount();
            if (amount != null) {
                Amount amountByName = getAmountByName(amount.getName());
                if (amountByName != null) {
                    product.setAmount(amountByName);
                }
            }
            Ingridient ingridient = product.getIngridient();
            if (ingridient != null) {
                Ingridient ingridientByName = getIngridientByName(ingridient.getName());
                if (ingridientByName != null) {
                    product.setIngridient(ingridientByName);
                }
            }
            productDAO.create(product);
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public Category getCategoryByID(final Integer id) {
        return categoryDAO.getByID(id);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public Recipe getRecipeByID(final Integer id) {
        return recipeDAO.getByID(id);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public Collection<Category> getAllCategory() {
        return categoryDAO.getAll();
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public Collection<Recipe> getAllRecipe() {
        return recipeDAO.getAll();
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void deleteRecipe(final Integer id) {
        recipeDAO.delete(id);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public Collection<String> getIngridientsByName(final String queryString) {
        Collection<String> list = null;
        if (queryString != null) {
            Query query = getSession().createQuery("select ingridient.name from Ingridient ingridient "
                    + "where ingridient.name like :name");
            query.setString("name", "%" + queryString + "%");
            query.setMaxResults(10);
            list = query.list();
        }
        return list;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public Collection<String> getAmountsByName(final String queryString) {
        Collection<String> list = null;
        if (queryString != null) {
            Query query = getSession().createQuery("select amount.name from Amount amount "
                    + "where amount.name like :name");
            query.setString("name", "%" + queryString + "%");
            query.setMaxResults(10);
            list = query.list();
        }
        return list;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public Ingridient getIngridientByName(final String ingridientName) {
        return ingridientDAO.getByName(ingridientName);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public Amount getAmountByName(final String amountName) {
        return amountDAO.getByName(amountName);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public Category getCategoryByName(final String categoryName) {
        return categoryDAO.getByName(categoryName);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public Recipe getRecipeByName(final String recipeName) {
        return recipeDAO.getByName(recipeName);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void createUser(UserAccount user) {
        userAccountDAO.create(user);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public UserAccount getUserAccountByName(final String userName) {
        return userAccountDAO.getByName(userName);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public UserAuthority getUserAuthorityById(final Integer id) {
        return userAuthorityDAO.getByID(id);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public Mark getMarkById(final Double markId) {
        Integer mark = markId.intValue();
        return markDAO.getByID(mark);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void saveOrUpdateRecipeMark(final RecipeMark recipeMark) {
        recipeMarkDAO.create(recipeMark);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public RecipeMark getRecipeMarkByRecipeAndUser(final Integer userId, final Integer recipeId) {
        if (userId != null && recipeId != null) {
            Query query = getSession().createQuery("from RecipeMark where recipe.id = :recipeId and userAccount.id = :userId");
            query.setParameter("recipeId", recipeId);
            query.setParameter("userId", userId);
            return (RecipeMark) query.uniqueResult();
        }
        return null;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public Collection<Recipe> getRecipesOrderByDate() {
        Query query = getSession().createQuery("from Recipe order by dateAdded desc").setMaxResults(9);
        Collection<Recipe> list = query.list();
        for (Recipe recipe : list) {
            getSession().evict(recipe);
        }
        return list;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void deleteRecipeMark(final Integer recipeMarkId) {
        recipeMarkDAO.delete(recipeMarkId);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void createFeedback(final Feedback feedback) {
        feedbackDAO.create(feedback);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public Collection<Recipe> getRecipesOrderByAvgMark() {
//        Query query = getSession().createQuery("select recipe from Recipe recipe join recipe.recipeMarks marks group by marks.recipe.id order by avg(marks.mark.id) desc").setMaxResults(9);
        Query query = getSession().createQuery("select recipe from Recipe recipe order by recipe.avgMark desc");
//        Criteria query = getSession()
//                .createCriteria(Recipe.class)
//                .createAlias("recipeMarks", "rm")
//                .setProjection(Projections.projectionList()
//                .add(Projections.groupProperty("rm.recipe.id"))
//                .add(Projections.avg("rm.mark.id"), "avgMark"))
//                .addOrder(Order.desc("avgMark"))
//                .setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
//        List list = criteria.list();
        return query.list();

    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public Collection<Recipe> getRecipeByStringAndIngridients(final String searchString, final Collection<String> ingridients) {
        StringBuilder sb = new StringBuilder("select r from Recipe r ");
        if (!ingridients.isEmpty()) {
            sb.append("join r.products pr where ");
            for (String s : ingridients) {
                sb.append("(pr.ingridient.name like '" + s + "') and ");
            }
        } else {
            sb.append("where ");
        }
        sb.append("(r.name like '" + searchString + "') ");
        if (!ingridients.isEmpty()) {
            sb.append("group by pr.recipe.id ");
        }
        System.out.println(sb.toString());
        Query query = getSession().createQuery(sb.toString());
        return query.list();
    }

    @Override
    public Double getAvgMarkByRecipe(final Recipe recipeToCalculate) {
        if (recipeToCalculate != null) {
            Query query = getSession().createQuery("select avg(marks.mark.id) from Recipe recipe join recipe.recipeMarks marks where recipe.id = :recipeId");
            query.setParameter("recipeId", recipeToCalculate.getId());
            return (Double) query.uniqueResult();
        }
        return null;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public Collection<Recipe> getRecipesByUser(final Integer id) {
        Query query = getSession().createQuery("select recipe from Recipe recipe where recipe.userAccount.id = :id order by recipe.dateAdded desc");
        query.setParameter("id", id);
        return query.list();
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public Collection<RecipeMark> getRecipeMarkByUser(Integer id) {
        Query query = getSession().createQuery("select rm from RecipeMark rm where rm.userAccount.id = :id order by rm.dateAdded");
        query.setParameter("id", id);
        return query.list();
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public Collection<Recipe> getRecipesLazy(int first, int pageSize, int categoryId) {
        Query query = getSession().createQuery("select r from Recipe r where r.category.id = :catId").setParameter("catId", categoryId).setFirstResult(first).setMaxResults(pageSize);
        return query.list();
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public int getRecipeCountInCategory(int categoryId) {
        Query query = getSession().createQuery("select count(r) from Recipe r where r.category.id = :catId").setParameter("catId", categoryId);
        Long lval = (Long) query.list().get(0);
        return lval.intValue();
    }
}
