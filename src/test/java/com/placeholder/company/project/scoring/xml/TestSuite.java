package com.placeholder.company.project.scoring.xml;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.google.common.base.MoreObjects;

@XmlRootElement( name = "testsuite" )
@XmlAccessorType( XmlAccessType.FIELD )
public class TestSuite {

	@XmlAttribute( name = "name" )
	private String name;

	@XmlAttribute( name = "time" )
	private Double time;

	@XmlAttribute( name = "tests" )
	private int tests;

	@XmlAttribute( name = "errors" )
	private int errors;

	@XmlAttribute( name = "skipped" )
	private int skipped;

	@XmlAttribute( name = "failures" )
	private int failures;

	@XmlElement( name = "testcase" )
	private List<TestCase> testCases;

	public void addTestCase( TestCase testCase ) {
		if ( testCase == null ) {
			return;
		}
		if ( this.testCases == null ) {
			this.testCases = new ArrayList<>();
		}
		this.testCases.add( testCase );
	}

	private TestSuite() {

	}

	public static class TestSuiteBuilder {

		private final TestSuite testSuite;

		public TestSuiteBuilder() {
			this.testSuite = new TestSuite();
		}

		public TestSuiteBuilder withName( String name ) {
			this.testSuite.name = name;
			return this;
		}

		public TestSuiteBuilder withTime( Double time ) {
			this.testSuite.time = time;
			return this;
		}

		public TestSuiteBuilder withTests( int tests ) {
			this.testSuite.tests = tests;
			return this;
		}

		public TestSuiteBuilder withErrors( int errors ) {
			this.testSuite.errors = errors;
			return this;
		}

		public TestSuiteBuilder withSkipped( int skipped ) {
			this.testSuite.skipped = skipped;
			return this;
		}

		public TestSuiteBuilder withFailures( int failures ) {
			this.testSuite.failures = failures;
			return this;
		}

		public TestSuiteBuilder withTestCases( List<TestCase> testCases ) {
			this.testSuite.testCases = testCases;
			return this;
		}

		public TestSuite build() {
			return testSuite;
		}
	}

	@Override
	public String toString() {
		return MoreObjects.toStringHelper( this ).omitNullValues()
				.add( "name", name )
				.add( "time", time )
				.add( "tests", tests )
				.add( "errors", errors )
				.add( "skipped", skipped )
				.add( "failures", failures )
				.add( "testCases", testCases )
				.toString();
	}
}
