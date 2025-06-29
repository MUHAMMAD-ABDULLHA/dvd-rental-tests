package com.dvd.rental;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import static org.junit.jupiter.api.Assertions.*;

import java.time.Duration;

public class DvdRentalTest {
    private WebDriver driver;
    private WebDriverWait wait;
    private final String baseUrl = "http://44.206.237.47:5000/"; // Replace with EC2 IP

    @BeforeEach
    public void setUp() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");
        options.addArguments("--disable-gpu");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        driver = new ChromeDriver(options);
        wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        driver.manage().window().maximize();
        driver.get(baseUrl);
        ((JavascriptExecutor) driver).executeScript("localStorage.setItem('role', 'customer');");
    }

    @AfterEach
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    public void testCustomerLoginSuccess() {
        driver.get(baseUrl + "/login.html?role=customer");
        driver.findElement(By.id("email")).sendKeys("customer@test.com");
        driver.findElement(By.id("password")).sendKeys("password123");
        driver.findElement(By.cssSelector("#loginForm button[type='submit']")).click();
        wait.until(ExpectedConditions.urlContains("customer.html"));
        assertTrue(driver.getCurrentUrl().contains("customer.html"));
    }

    @Test
    public void testCustomerLoginFailure() {
        driver.get(baseUrl + "/login.html?role=customer");
        driver.findElement(By.id("email")).sendKeys("wrong@test.com");
        driver.findElement(By.id("password")).sendKeys("wrong");
        driver.findElement(By.cssSelector("#loginForm button[type='submit']")).click();
        wait.until(ExpectedConditions.alertIsPresent());
        assertEquals("Login failed: User not found", driver.switchTo().alert().getText());
        driver.switchTo().alert().accept();
    }

    @Test
    public void testManagerLoginSuccess() {
        driver.get(baseUrl);
        ((JavascriptExecutor) driver).executeScript("localStorage.setItem('role', 'manager');");
        driver.get(baseUrl + "/login.html?role=manager");
        driver.findElement(By.id("email")).sendKeys("manager@test.com");
        driver.findElement(By.id("password")).sendKeys("password123");
        driver.findElement(By.cssSelector("#loginForm button[type='submit']")).click();
        wait.until(ExpectedConditions.urlContains("manager.html"));
        assertTrue(driver.getCurrentUrl().contains("manager.html"));
    }

    @Test
    public void testManagerLoginFailure() {
        driver.get(baseUrl);
        ((JavascriptExecutor) driver).executeScript("localStorage.setItem('role', 'manager');");
        driver.get(baseUrl + "/login.html?role=manager");
        driver.findElement(By.id("email")).sendKeys("wrong@test.com");
        driver.findElement(By.id("password")).sendKeys("wrong");
        driver.findElement(By.cssSelector("#loginForm button[type='submit']")).click();
        wait.until(ExpectedConditions.alertIsPresent());
        assertEquals("Login failed: User not found", driver.switchTo().alert().getText());
        driver.switchTo().alert().accept();
    }

    @Test
    public void testCustomerRegistration() {
        driver.get(baseUrl + "/register.html?role=customer");
        driver.findElement(By.id("id")).sendKeys("C002");
        driver.findElement(By.id("name")).sendKeys("New Customer");
        driver.findElement(By.id("email")).sendKeys("newcustomer@test.com");
        driver.findElement(By.id("password")).sendKeys("password123");
        driver.findElement(By.id("contactNo")).sendKeys("1234567890");
        driver.findElement(By.id("age")).sendKeys("25");
        driver.findElement(By.id("address")).sendKeys("123 Test St");
        driver.findElement(By.id("registrationNo")).sendKeys("REG002");
        driver.findElement(By.id("dob")).sendKeys("1998-01-01");
        driver.findElement(By.id("gender")).sendKeys("Male");
        driver.findElement(By.cssSelector("#registerForm button[type='submit']")).click();
        wait.until(ExpectedConditions.alertIsPresent());
        assertEquals("Customer account created successfully!", driver.switchTo().alert().getText());
        driver.switchTo().alert().accept();
        wait.until(ExpectedConditions.urlContains("login.html"));
    }

    @Test
    public void testManagerRegistration() {
        driver.get(baseUrl);
        ((JavascriptExecutor) driver).executeScript("localStorage.setItem('role', 'manager');");
        driver.get(baseUrl + "/register.html?role=manager");
        driver.findElement(By.id("id")).sendKeys("M002");
        driver.findElement(By.id("name")).sendKeys("New Manager");
        driver.findElement(By.id("email")).sendKeys("newmanager@test.com");
        driver.findElement(By.id("password")).sendKeys("password123");
        driver.findElement(By.id("contactNo")).sendKeys("1234567890");
        driver.findElement(By.id("department")).sendKeys("Sales");
        driver.findElement(By.cssSelector("#registerForm button[type='submit']")).click();
        wait.until(ExpectedConditions.alertIsPresent());
        assertEquals("Manager account created successfully!", driver.switchTo().alert().getText());
        driver.switchTo().alert().accept();
        wait.until(ExpectedConditions.urlContains("login.html"));
    }

    @Test
    public void testAddDvd() {
        driver.get(baseUrl);
        ((JavascriptExecutor) driver).executeScript("localStorage.setItem('role', 'manager');");
        driver.get(baseUrl + "/login.html?role=manager");
        driver.findElement(By.id("email")).sendKeys("manager@test.com");
        driver.findElement(By.id("password")).sendKeys("password123");
        driver.findElement(By.cssSelector("#loginForm button[type='submit']")).click();
        wait.until(ExpectedConditions.urlContains("manager.html"));
        driver.findElement(By.id("title")).sendKeys("New DVD");
        driver.findElement(By.id("genre")).sendKeys("Comedy");
        driver.findElement(By.id("stock")).sendKeys("5");
        driver.findElement(By.id("releaseDate")).sendKeys("2024-01-01");
        driver.findElement(By.id("price")).sendKeys("3");
        driver.findElement(By.cssSelector("#addDvdForm button[type='submit']")).click();
        wait.until(ExpectedConditions.alertIsPresent());
        assertEquals("DVD Added!", driver.switchTo().alert().getText());
        driver.switchTo().alert().accept();
        wait.until(ExpectedConditions.textToBePresentInElementLocated(By.id("dvdList"), "New DVD"));
        assertTrue(driver.findElement(By.id("dvdList")).getText().contains("New DVD"));
    }

    @Test
    public void testUpdateDvd() {
        driver.get(baseUrl);
        ((JavascriptExecutor) driver).executeScript("localStorage.setItem('role', 'manager');");
        driver.get(baseUrl + "/login.html?role=manager");
        driver.findElement(By.id("email")).sendKeys("manager@test.com");
        driver.findElement(By.id("password")).sendKeys("password123");
        driver.findElement(By.cssSelector("#loginForm button[type='submit']")).click();
        wait.until(ExpectedConditions.urlContains("manager.html"));
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[contains(text(), 'Update')]")));
        driver.findElement(By.xpath("//button[contains(text(), 'Update')]")).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("updateDvdModal")));
        driver.findElement(By.id("updateTitle")).clear();
        driver.findElement(By.id("updateTitle")).sendKeys("Updated DVD");
        driver.findElement(By.cssSelector("#updateDvdForm button[type='submit']")).click();
        wait.until(ExpectedConditions.alertIsPresent());
        assertEquals("DVD updated successfully!", driver.switchTo().alert().getText());
        driver.switchTo().alert().accept();
        wait.until(ExpectedConditions.textToBePresentInElementLocated(By.id("dvdList"), "Updated DVD"));
        assertTrue(driver.findElement(By.id("dvdList")).getText().contains("Updated DVD"));
    }

    @Test
    public void testDeleteDvd() {
        driver.get(baseUrl);
        ((JavascriptExecutor) driver).executeScript("localStorage.setItem('role', 'manager');");
        driver.get(baseUrl + "/login.html?role=manager");
        driver.findElement(By.id("email")).sendKeys("manager@test.com");
        driver.findElement(By.id("password")).sendKeys("password123");
        driver.findElement(By.cssSelector("#loginForm button[type='submit']")).click();
        wait.until(ExpectedConditions.urlContains("manager.html"));
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[contains(text(), 'Delete')]")));
        driver.findElement(By.xpath("//button[contains(text(), 'Delete')]")).click();
        wait.until(ExpectedConditions.alertIsPresent());
        assertEquals("DVD deleted successfully!", driver.switchTo().alert().getText());
        driver.switchTo().alert().accept();
        assertFalse(driver.findElement(By.id("dvdList")).getText().contains("New DVD"));
    }

    @Test
    public void testRentDvd() {
        driver.get(baseUrl + "/login.html?role=customer");
        driver.findElement(By.id("email")).sendKeys("customer@test.com");
        driver.findElement(By.id("password")).sendKeys("password123");
        driver.findElement(By.cssSelector("#loginForm button[type='submit']")).click();
        wait.until(ExpectedConditions.urlContains("customer.html"));
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[contains(text(), 'Rent')]")));
        driver.findElement(By.xpath("//button[contains(text(), 'Rent')]")).click();
        wait.until(ExpectedConditions.alertIsPresent());
        assertEquals("DVD rented successfully!", driver.switchTo().alert().getText());
        driver.switchTo().alert().accept();
        wait.until(ExpectedConditions.textToBePresentInElementLocated(By.id("rentalList"), "Test DVD"));
        assertTrue(driver.findElement(By.id("rentalList")).getText().contains("Test DVD"));
    }
}