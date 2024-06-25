package Managers;

import Models.*;
import java.util.Observable;

import java.util.ArrayList;
import java.util.List;

public class CategoryManager extends Observable {
    private List<Category> categories = new ArrayList<>();
    private CategoryDAO categoryDAO = new CategoryDAO();

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
        setChanged();
        notifyObservers(categories);
    }

    public void addCategory(Category category) {
        categoryDAO.insertCategory(category);
        categories.add(category);
        setChanged();
        notifyObservers(categories);
    }

    public void updateCategory(Category updatedCategory) {
        categoryDAO.updateCategory(updatedCategory);
        for (int i = 0; i < categories.size(); i++) {
            Category category = categories.get(i);
            if (category.getId().equals(category.getId())) {
                categories.set(i, updatedCategory);
                break;
            }
        }
        setChanged();
        notifyObservers(categories);
    }

    public void removeProduct(Category category) {
        categoryDAO.deleteCategory(category);
        categories.remove(category);
        setChanged();
        notifyObservers(categories);
    }

    public void loadCategoriesFromDatabase() {
        List<Category> categoriesFromDB = categoryDAO.getAllCategories();
        setCategories(categoriesFromDB);
    }
}
