package de.oglimmer.view;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import de.oglimmer.model.Person;
import de.oglimmer.service.PersonService;

@Named
@RequestScoped
public class ListView {

	private List<Person> persons;
	private String currentPage;
	private String searchSurname;
	private String searchFirstname;
	private Integer[] pagesArray;
	private String sort;
	private String sortOrder;

	@Inject
	private PersonService personService;

	public List<Person> getPersons() {
		return persons;
	}

	public Integer[] getPagesArray() {
		return pagesArray;
	}

	public String getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(String currentPage) {
		this.currentPage = currentPage;
	}

	public String getSearchSurname() {
		return searchSurname;
	}

	public void setSearchSurname(String searchSurname) {
		this.searchSurname = searchSurname;
	}

	public String getSearchFirstname() {
		return searchFirstname;
	}

	public void setSearchFirstname(String searchFirstname) {
		this.searchFirstname = searchFirstname;
	}

	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}

	public String getSortOrder() {
		return sortOrder;
	}

	public void setSortOrder(String sortOrder) {
		this.sortOrder = sortOrder;
	}

	public void search() throws SQLException {
		switchPage(currentPage);
	}

	public void initLoad() throws SQLException {
		currentPage = "1";
		switchPage(currentPage);
	}

	public void sortByCol(String colName) throws SQLException {
		if (sort.equals(colName)) {
			if (sortOrder.equals("asc")) {
				sortOrder = "desc";
			} else {
				sortOrder = "asc";
			}
		} else {
			sort = colName;
			sortOrder = "asc";
		}
		switchPage(currentPage);
	}

	public void switchPage(String pageNo) throws SQLException {

		long numRecords = personService.listSize(searchSurname, searchFirstname);
		System.out.println("numRecords:" + numRecords);

		int page = Integer.parseInt(pageNo);

		int pageStart = Math.max(1, page - 3);
		int pageEnd = Math.max(1, Math.min(page + 3, (int) Math.ceil(numRecords / 10f)));
		page = Math.min(page, pageEnd);
		pageStart = Math.min(page, pageStart);
		System.out.println(pageStart + " to " + pageEnd + " // " + page);

		persons = new ArrayList<>(personService.list(searchSurname, searchFirstname, page, sort, sortOrder));

		pagesArray = IntStream.rangeClosed(pageStart, pageEnd).boxed().toArray(Integer[]::new);
		currentPage = Integer.toString(page);
	}
}