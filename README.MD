# Store Front Demo

My store front demo has two parts. There is customer side, where products can be viewed and orders can be placed, and the business side where employees can setup products and process orders. 

Customer http://localhost:8080/storefront/listproducts
(Note: links at the top to get around in the shop)

     View the product available – (must create an account to make a purchase) 

        o	Can view the product info

     Register an account - basic info with username and password (username must not already
    exist)

        o	Once you create an account you must login
        o	Account info is there - can add credit card or make a purchase now

     Login (ssmith/password) (shows you are signed in)-allows you to view and update account Information

        o	Change your password 
        o	Add a credit card (card type) - add multiple cards
        o	View past orders (status & details)
        o	See what is in your cart - displays the number of items
            	Click on the items in cart or the cart to view cart
            	Can clear your cart or remove items
            	Can edit the number of items (number of items are updated)

        o	Checkout and create an order - (new order will show in purchase history)
            	Pulls in your account info - (can override) credit card must not be expired

Employee http://localhost:8080 main menu - with Company info
(Note: links at the top list menus to go back to main menu)
     Setup Employee with a login

        o	Add, change, or delete an employee
            	Set employee with basic information
            	Assign employees job role
            	Enable login - if disable must contact Admin to enable 
            	 Add a password (not required to update employee)
                •	if password is set the same as the username (forces user to create a password at first login)

     Manage Products

        o	Add, change, or view a product
            	Add a product (Image URL-product6.JPG) - must have price (do not need QTY)
            	Change a product - to status Available or Discontinued) and On Hand Qty (Allocated and Available is calculated based on orders)
            	Product marked new or discontinued do not show on Storefront.

     Process Orders

        o	View and update orders
            	Find customer by last name & refresh list

        o	Order status can be changed to process, cancel or ship - inventory on products recalculate when cancelled or shipped.

My Storefront Project Preloaded USERS

Customers

    Michael Weston

    Username - weston
    Password - password

    Stacey Smith (multiple credit cards)

    Username - smith
    Password - password

    Sam Axe

    Username - saxe
    Password - password

Employees

    David Jones (Administrator)

    Username - djones
    Password - password

    Tim Landon (Manager)

    Username - tlandon
    Password - password

    Susan Black (User) - force change in password

    Username - sblack
    Password - black
