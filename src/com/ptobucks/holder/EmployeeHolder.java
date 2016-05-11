package com.ptobucks.holder;

import java.util.Vector;

import com.ptobucks.model.Employee;

public class EmployeeHolder {
	
	public static Vector<Employee> employeeModels = new Vector<Employee>();
	
	public static Vector<Employee> getAllEmployees(){
		return employeeModels;
	}
	
	public static void setAllEmployees(Vector<Employee> employeeModels){
		EmployeeHolder.employeeModels = employeeModels;
	}
	
	public static Employee getEmployee(int pos){
		return employeeModels.elementAt(pos);
	}
	
	public static void setEmployeeList(Employee employee){
		EmployeeHolder.employeeModels.addElement(employee);
	}
	
	public static Employee findEmployeeWithId(String id){
		Employee employee = new Employee();
		for (Employee emp : employeeModels) {
			if (emp.getId().equals(id)) {
				employee = emp;
				break;
			}
		}
		return employee;
	}
	
	public static void removeEmployeeList(){
		EmployeeHolder.employeeModels.removeAllElements();
	}

}
