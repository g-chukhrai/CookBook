package by.chuger.cookbook.view.bean;

import by.chuger.cookbook.model.dao.Facade;
import by.chuger.cookbook.model.domain.*;
import by.chuger.cookbook.utils.MessageUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
import javax.imageio.ImageIO;
import javax.inject.Named;
import javax.servlet.ServletContext;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Component
@Named("addRecipeMB")
@Scope("view")
public class AddRecipeMB implements Serializable {

    @Autowired
    private Facade facade;
    private Recipe recipe;
    private Product product;
    private static Collection<Category> mainCategories;
    private List<String> uploadedFilenames;
    private List<Product> products;
    private static String RECIPE_PATH;

    public Collection<String> getUploadedFilenames() {
        return uploadedFilenames;
    }

    public StreamedContent tempImage(UploadedFile file) throws IOException {
        StreamedContent image = new DefaultStreamedContent(file.getInputstream(), "image/jpeg");
        return image;
    }

    public List<Product> getProducts() {
        if (recipe != null) {
            products = new ArrayList();
            for (Product existedProduct : recipe.getProducts()) {
                products.add(existedProduct);
            }
        }
        return products;
    }

    public AddRecipeMB() {
    }

    public Recipe getRecipe() {
        if (recipe == null) {
            initRecipe();
        }
        return recipe;
    }

    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
    }

    public Product getProduct() {
        if (product == null) {
            initProduct();
        }
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Collection<Category> getCategories() {
        if (mainCategories == null) {
            mainCategories = facade.getAllCategory();
        }
        return mainCategories;
    }

    public String createNew() {
        initProduct();
        return null;
    }

    public void submitCreate(ActionEvent event) throws UnsupportedEncodingException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication.getPrincipal();
        if (!validateRecipe(principal)) {
            return;
        }

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        UserAccount userAccount = facade.getUserAccountByName(userDetails.getUsername());
        if (userAccount == null) {
            MessageUtils.showMessage("Authentification problem", "User Account not found.");
            return;
        }

        if (recipe.getDescription().equals(MessageUtils.getMessage("tooltip.inputRecipeDescription"))) {
            recipe.setDescription(null);
        }
        if (recipe.getCookTime().equals(0)) {
            recipe.setCookTime(null);
        }
        recipe.setUserAccount(userAccount);
        recipe.setCategory(facade.getCategoryByID(recipe.getCategory().getId()));

        // TODO: solve this bug
        recipe.setProcess(recipe.getProcess().replaceAll("resources", "/CookBook/resources"));
        updateProcess();

        String imagesString = "";
        for (String filename : uploadedFilenames) {
            imagesString += filename + ";";
        }
        recipe.setImages(MessageUtils.isNotEmpty(imagesString) ? imagesString : "noimage.png;");

        facade.createRecipe(recipe);//ss
        String redirectUrl = MessageFormat.format("category/{0}/recipe/{1}", recipe.getCategory().getId(), recipe.getId());
        //clean because using view scoped bean
        initRecipe();
        initProduct();
        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect(redirectUrl);
        } catch (IOException ex) {
            MessageUtils.showMessage("Redirect", "IOException.");
        }
    }

    private boolean validateRecipe(final Object principal) {
        boolean isValid = true;
        if (principal instanceof String) {
            isValid = false;
            MessageUtils.showMessage("Authentification problem", "You mus be logged in to create recipe.");
        } else if (principal instanceof UserDetails == false) {
            isValid = false;
            MessageUtils.showMessage("Authentification problem", "Something wrong.");
        }
        if (recipe.getName().equals(MessageUtils.getMessage("tooltip.inputRecipeName"))) {
            isValid = false;
            MessageUtils.addError("recipeName", "warning.recipeName");
        }
        if (recipe.getProcess().equals(MessageUtils.getMessage("tooltip.inputRecipeProcess"))) {
            isValid = false;
            MessageUtils.addError("recipeProcess", "warning.recipeProcess");
        }
        return isValid;
    }

    private void initProduct() {
        product = new Product();
        product.setIngridient(new Ingridient());
        product.setAmount(new Amount());
        product.setAmountSize(new String());
        product.setRecipe(getRecipe());
    }

    private void initRecipe() {
        recipe = new Recipe();
        recipe.setCategory(new Category());
        uploadedFilenames = new ArrayList<String>();
        products = new ArrayList<Product>();
    }

    public Collection<String> completeIngridient(String queryString) {
        return facade.getIngridientsByName(queryString);
    }

    public Collection<String> completeAmount(String queryString) {
        return facade.getAmountsByName(queryString);
    }

    public void handleFileUpload(FileUploadEvent event) {
        UploadedFile file = event.getFile();
        String fileName = uploadFile(file);
        uploadedFilenames.add(fileName);
        MessageUtils.showMessage("Succesful", event.getFile().getFileName() + " is temp uploaded.");
    }

    private String uploadFile(final UploadedFile file) {

        try {
            String fileName = new String(file.getFileName().getBytes(), "UTF-8");
            String newFileName = getNewFilename(RECIPE_PATH + fileName);

            FileOutputStream fos = new FileOutputStream(new File(newFileName));
            InputStream is = file.getInputstream();
            int BUFFER_SIZE = 8192;
            byte[] buffer = new byte[BUFFER_SIZE];
            int a;
            while (true) {
                a = is.read(buffer);
                if (a < 0) {
                    break;
                }
                fos.write(buffer, 0, a);
                fos.flush();
            }
            fos.close();
            is.close();
            MessageUtils.showMessage("Succesful", fileName + " is uploaded.");
            return newFileName;
        } catch (UnsupportedEncodingException e) {
            MessageUtils.showMessage("Fail", "Encoding filename.");
        } catch (IOException e) {
            MessageUtils.showMessage("Fail", "Fail creatating uploading.");
        }
        return null;
    }

    public void removeImage(final String filename) {
        try {
            File file = new File(RECIPE_PATH + filename);
            file.delete();
        } catch (SecurityException e) {
        }
    }
    private String editorText;

    public String getEditorText() {
        System.out.println("GET + " + editorText);
        return editorText;
    }

    public void setEditorText(String editorText) {
        this.editorText = editorText;
        System.out.println(editorText);
    }

    public void addImageToEditor(final String filename) {
        String appendString = "<br><img src=\"/CookBook/resources/recipes/" + filename + "\" width=\"250\"/>";
        System.out.println(appendString);
        editorText += appendString;
        System.out.println(editorText);

    }

    public void makeTitle(final String filename) {
        try {
            int curPos = uploadedFilenames.indexOf(filename);
            uploadedFilenames.remove(curPos);
            uploadedFilenames.add(0, filename);
        } catch (NullPointerException e) {
        } catch (UnsupportedOperationException e) {
        } catch (IndexOutOfBoundsException e) {
        }
    }

    public void makeUp(final String filename) {
        try {
            int curPos = uploadedFilenames.indexOf(filename);
            String prev = uploadedFilenames.get(curPos - 1);
            uploadedFilenames.remove(curPos - 1);
            uploadedFilenames.add(curPos - 1, filename);
            uploadedFilenames.remove(curPos);
            uploadedFilenames.add(curPos, prev);
        } catch (NullPointerException e) {
        } catch (UnsupportedOperationException e) {
        } catch (IndexOutOfBoundsException e) {
        }
    }

    public void makeDown(final String filename) {
        try {
            int curPos = uploadedFilenames.indexOf(filename);
            String next = uploadedFilenames.get(curPos + 1);
            uploadedFilenames.remove(curPos);
            uploadedFilenames.add(curPos, next);
            uploadedFilenames.remove(curPos + 1);
            uploadedFilenames.add(curPos + 1, filename);
        } catch (NullPointerException e) {
        } catch (UnsupportedOperationException e) {
        } catch (IndexOutOfBoundsException e) {
        }
    }

    public void updateProcess() {
        String process = recipe.getProcess();
        for (Element e : Jsoup.parse(process).select("img")) {
//            String htmlImgPath = e.toString();
            String attr = e.attr("src");
            if (attr.matches("http.*")) {
                System.out.println(attr);
                try {
                    BufferedImage img = ImageIO.read(new URL(attr));
                    String filename = getNewFilename(String.valueOf(img.hashCode())) + ".jpg";
                    System.out.println(filename);
                    ImageIO.write(img, "JPG", new File(RECIPE_PATH + filename));
                    uploadedFilenames.add(filename);
                    process = process.replaceAll(attr, "/CookBook/resources/recipes/" + filename);
                    System.out.println(process);

                } catch (IOException ex) {
                }
            }
        }
        recipe.setProcess(process);
        System.out.println(process);
    }

    private String getNewFilename(String oldFilename) {
        if (RECIPE_PATH == null) {
            ServletContext servletContext = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
            RECIPE_PATH = MessageFormat.format("{0}/{1}/{2}/", servletContext.getRealPath(""), "resources", "recipes");
        }
        String newFilename = oldFilename;
        int index = 1;
        while (new File(RECIPE_PATH + newFilename).exists()) {
            index++;
            String prefix = "(" + String.valueOf(index) + ")";
            newFilename = prefix + oldFilename;
        }
        return newFilename;
    }

    public void processChanged(ValueChangeEvent event){
        System.out.println("ALOE");
    }
}
